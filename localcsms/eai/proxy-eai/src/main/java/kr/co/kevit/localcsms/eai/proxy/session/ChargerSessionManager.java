package kr.co.kevit.localcsms.eai.proxy.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.eai.proxy.config.ProxyProperties;
import kr.co.kevit.localcsms.eai.proxy.mq.DroppingRequestConsumer;
import kr.co.kevit.localcsms.eai.proxy.mq.LhChargerNotifyConsumer;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.localcsms.system.entity.domain.ConnConfig;
import kr.co.kevit.localcsms.system.process.ConnConfigService;

/**
 * 기동 시 {@code ConnConfig}(TB_SYCN001, 싱글턴)를 조회해 LH/CPO 를 판정하고, 그에 따라
 * {@link Cs0Session}(항상 1개) + (CPO 모드일 때만) 충전기별 {@link ChargerSession} 을 기동한다.
 *
 * <ul>
 * <li>{@code localOperationType == "OPMD01"}(로컬/LH 모드) — {@code lhCsmsAddress + "/" + localSystemId}
 * 로 CS0 세션 1개만 기동(충전기별 개별 WS 연결은 만들지 않음). 대신 충전기별로 {@code req.<cpCsId>} 큐를
 * {@code ocpp16.notify} 익스체인지에 라우팅키 {@code <cpCsId>.#} 로 바인딩해 구독하고,
 * {@link LhChargerNotifyConsumer} 가 action 별로 분기 처리한다(BootNotification →
 * DataTransfer.req(ChildBootNotification) 변환 후 CS0 로 전송, 그 외는 현재 단계에서 무시).</li>
 * <li>그 외(CPO 모드) — {@code cpoCsmsAddress + "/" + localSystemId} 로 CS0 세션 1개 +
 * 충전기별로 {@code cpoCsmsAddress + "/" + cpoCpCsId}(대상 CPO 가 부여한 식별자, 우리 내부
 * {@code cpCsId} 와 다름) 로 개별 세션 기동. {@code cpoCpCsId} 가 비어있는 충전기는 접속하지 않고
 * {@code req.<cpCsId>} 큐에 {@link DroppingRequestConsumer} 만 붙여 유입 메시지를 버린다(daemon 은
 * 매핑 여부를 모른 채 발행할 수 있으므로 큐 적체 방지). 큐 이름 등 daemon 과의 내부 계약은 계속
 * {@code cpCsId}("cpId-csId")를 쓴다.</li>
 * </ul>
 *
 * <p>
 * 운영 중 {@code ConnConfig}/충전기 추가·변경은 재기동으로 반영한다(동적 재조회 없음) — CPO 모드 접속도
 * 기동 시 조회된 목록으로만 연결하고, 도중에 추가된 충전기는 연동하지 않는다.
 * </p>
 *
 * @author bckim
 */
@Component
public class ChargerSessionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerSessionManager.class);

    /** TB_SYCO001.OPMD00 하위 코드 — 로컬(LH) 모드. */
    private static final String OPERATION_TYPE_LOCAL_LH = "OPMD01";

    private final ChargingStationService chargingStationService;
    private final ConnConfigService connConfigService;
    private final RechargingService rechargingService;
    private final ProxyProperties props;
    private final SSLSocketFactory sslSocketFactory;
    private final ConnectionFactory rabbitConnectionFactory;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    private final List<ChargerSession> sessions = new CopyOnWriteArrayList<>();
    private final List<SimpleMessageListenerContainer> lhListenerContainers = new CopyOnWriteArrayList<>();
    private final List<SimpleMessageListenerContainer> cpoDropListenerContainers = new CopyOnWriteArrayList<>();
    /** cpCsId(내부) → cpoCpCsId(대상 CPO 식별자) — CPO 모드 기동 시 1회 구성, 진단용으로 보관. */
    private final Map<String, String> cpoCpCsIdByCpCsId = new HashMap<>();
    private Cs0Session cs0Session;

    public ChargerSessionManager(ChargingStationService chargingStationService, ConnConfigService connConfigService,
            RechargingService rechargingService, ProxyProperties props, SSLSocketFactory sslSocketFactory,
            ConnectionFactory rabbitConnectionFactory, RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.chargingStationService = chargingStationService;
        this.connConfigService = connConfigService;
        this.rechargingService = rechargingService;
        this.props = props;
        this.sslSocketFactory = sslSocketFactory;
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    /** 애플리케이션 완전 기동 후(빈 초기화 완료) 세션들을 띄운다. */
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        ConnConfig connConfig = connConfigService.retrieveConnConfig();
        if (connConfig == null) {
            LOGGER.error("[proxy-eai] ConnConfig(TB_SYCN001) 조회 실패 — 세션을 기동하지 않습니다.");
            return;
        }

        boolean isLh = OPERATION_TYPE_LOCAL_LH.equals(connConfig.getLocalOperationType());
        String base = isLh ? connConfig.getLhCsmsAddress() : connConfig.getCpoCsmsAddress();
        String localSystemId = connConfig.getLocalSystemId();

        if (!StringUtils.hasText(base) || !StringUtils.hasText(localSystemId)) {
            LOGGER.error("[proxy-eai] ConnConfig 설정 누락(mode={}, base={}, localSystemId={}) — 세션을 기동하지 않습니다.",
                    isLh ? "LH" : "CPO", base, localSystemId);
            return;
        }

        LOGGER.info("[proxy-eai] 운영모드={} base={} localSystemId={}", isLh ? "LH" : "CPO", base, localSystemId);

        // CS0 — 로컬 시스템 대표 세션. LH/CPO 무관하게 항상 1개.
        startCs0(buildUrl(base, localSystemId), localSystemId, connConfig.getLocalSystemSn());

        List<ChargingStationDto> chargers = retrieveAllChargers();

        if (isLh) {
            if (chargers.isEmpty()) {
                LOGGER.warn("[proxy-eai] 등록된 충전기가 없습니다 — CS0 세션만 유지합니다.");
                return;
            }
            LOGGER.info("[proxy-eai] LH 모드 — 충전기 {}대 notify 큐 구독 시작(req.<cpCsId>)", chargers.size());
            for (ChargingStationDto cs : chargers) {
                String cpCsId = cs.getCpId() + "-" + cs.getCsId();
                try {
                    declareLhNotifyQueue(cpCsId);
                    startLhChargerListener(cpCsId);
                } catch (Exception e) {
                    LOGGER.error("[proxy-eai] LH notify 큐 구독 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
                }
            }
            LOGGER.info("[proxy-eai] LH notify 큐 구독 {}건 기동 완료", lhListenerContainers.size());
            return;
        }

        // CPO 모드 — 등록된 충전기 전체를 개별 세션으로 추가 기동. 접속은 cpoCpCsId(대상 CPO 식별자) 기준,
        // 매핑 없는 충전기는 접속하지 않고 req.<cpCsId> 를 드롭 컨슈머로만 비워준다.
        if (chargers.isEmpty()) {
            LOGGER.warn("[proxy-eai] 등록된 충전기가 없습니다 — CS0 세션만 유지합니다.");
            return;
        }

        LOGGER.info("[proxy-eai] 충전기 {}대 조회 완료 — 세션 기동 시작", chargers.size());
        for (ChargingStationDto cs : chargers) {
            String cpCsId = cs.getCpId() + "-" + cs.getCsId();
            String cpoCpCsId = cs.getCpoCpCsId();
            try {
                declareChargerQueues(cpCsId);
                if (!StringUtils.hasText(cpoCpCsId)) {
                    LOGGER.warn("[proxy-eai] cpoCpCsId 매핑 없음 — 접속하지 않고 req.{} 드롭 컨슈머만 등록 (cpCsId={})",
                            cpCsId, cpCsId);
                    startCpoDropConsumer(cpCsId);
                    continue;
                }
                cpoCpCsIdByCpCsId.put(cpCsId, cpoCpCsId);
                ChargerSession session = new ChargerSession(cpCsId, buildUrl(base, cpoCpCsId), props,
                        sslSocketFactory, rabbitConnectionFactory, rabbitTemplate);
                sessions.add(session);
                Thread thread = new Thread(session, "proxy-eai-" + cpCsId);
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                LOGGER.error("[proxy-eai] 세션 기동 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
            }
        }
        LOGGER.info("[proxy-eai] 충전기 세션 {}개 기동(매핑 없음 {}개는 드롭 컨슈머만) — CS0 포함 총 {}개", sessions.size(),
                cpoDropListenerContainers.size(), sessions.size() + 1);
    }

    private void startCs0(String url, String localSystemId, String localSystemSn) {
        cs0Session = new Cs0Session(url, props, sslSocketFactory, rabbitConnectionFactory, amqpAdmin, localSystemId,
                localSystemSn);
        Thread thread = new Thread(cs0Session, "proxy-eai-CS0");
        thread.setDaemon(true);
        thread.start();
        LOGGER.info("[proxy-eai] CS0 세션 기동 url={}", url);
    }

    private String buildUrl(String base, String identifier) {
        String trimmedBase = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        return trimmedBase + "/" + identifier;
    }

    /** 등록된 충전기 전체 목록 조회 — 버전/조건 필터 없이 페이징도 없이 한 번에 (기동 시 1회 조회). */
    private List<ChargingStationDto> retrieveAllChargers() {
        ChargingStationSearchCond cond = new ChargingStationSearchCond();
        cond.setPageItemSize(Integer.MAX_VALUE);
        Page<ChargingStationDto> page = chargingStationService.retrieveChargingStationBySearchCond(cond);
        return page != null && page.getResult() != null ? page.getResult() : List.of();
    }

    private void declareChargerQueues(String cpCsId) {
        boolean durable = props.getQueue().isDurable();
        Queue requestQueue = new Queue(props.getQueue().getRequestPrefix() + cpCsId, durable);
        Queue responseQueue = new Queue(props.getQueue().getResponsePrefix() + cpCsId, durable);
        amqpAdmin.declareQueue(requestQueue);
        amqpAdmin.declareQueue(responseQueue);
    }

    /**
     * LH 모드용 — {@code req.<cpCsId>} 큐를 {@code ocpp16.notify} 익스체인지에
     * 라우팅키 {@code <cpCsId>.#} 로 바인딩해, 해당 충전기의 notify 메시지만 받도록 한다.
     * 익스체인지 선언 파라미터는 {@link Cs0Session} 이 쓰는 것과 동일해야 하므로 동일한
     * {@code props.getNotify()} 설정을 그대로 사용한다.
     */
    private void declareLhNotifyQueue(String cpCsId) {
        ProxyProperties.Notify notifyCfg = props.getNotify();
        TopicExchange exchange = new TopicExchange(notifyCfg.getExchange(), notifyCfg.isDurable(), false);
        Queue queue = new Queue(props.getQueue().getRequestPrefix() + cpCsId, props.getQueue().isDurable());
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(cpCsId + ".#"));
    }

    private void startLhChargerListener(String cpCsId) {
        String queueName = props.getQueue().getRequestPrefix() + cpCsId;
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConnectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(new LhChargerNotifyConsumer(cpCsId, cs0Session, rechargingService));
        container.start();
        lhListenerContainers.add(container);
    }

    /** CPO 모드 — cpoCpCsId 매핑 없는 충전기의 req.<cpCsId> 를 소비만 하고 버리는 컨슈머 기동. */
    private void startCpoDropConsumer(String cpCsId) {
        String queueName = props.getQueue().getRequestPrefix() + cpCsId;
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitConnectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(new DroppingRequestConsumer(cpCsId));
        container.start();
        cpoDropListenerContainers.add(container);
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("[proxy-eai] 세션 종료 중... (충전기 세션 {}개 + LH notify 구독 {}개 + 드롭 컨슈머 {}개 + CS0)",
                sessions.size(), lhListenerContainers.size(), cpoDropListenerContainers.size());
        lhListenerContainers.forEach(SimpleMessageListenerContainer::stop);
        cpoDropListenerContainers.forEach(SimpleMessageListenerContainer::stop);
        sessions.forEach(ChargerSession::stop);
        if (cs0Session != null) {
            cs0Session.stop();
        }
    }
}
