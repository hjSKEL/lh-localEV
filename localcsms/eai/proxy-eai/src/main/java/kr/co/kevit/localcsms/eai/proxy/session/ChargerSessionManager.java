package kr.co.kevit.localcsms.eai.proxy.session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PreDestroy;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationDto;
import kr.co.kevit.localcsms.charger.entity.shared.ChargingStationSearchCond;
import kr.co.kevit.localcsms.charger.process.ChargingStationService;
import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.eai.proxy.config.ProxyProperties;

/**
 * 기동 시 등록된 충전기 전체 목록을 1회 조회하여 그 수만큼 {@link ChargerSession} 스레드를 기동한다.
 *
 * <p>
 * 운영 중 충전기 추가/삭제는 재기동으로 반영한다(동적 재조회 없음).
 * </p>
 *
 * @author bckim
 */
@Component
public class ChargerSessionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargerSessionManager.class);

    private final ChargingStationService chargingStationService;
    private final ProxyProperties props;
    private final SSLSocketFactory sslSocketFactory;
    private final ConnectionFactory rabbitConnectionFactory;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    private final List<ChargerSession> sessions = new CopyOnWriteArrayList<>();

    public ChargerSessionManager(ChargingStationService chargingStationService, ProxyProperties props,
            SSLSocketFactory sslSocketFactory, ConnectionFactory rabbitConnectionFactory,
            RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.chargingStationService = chargingStationService;
        this.props = props;
        this.sslSocketFactory = sslSocketFactory;
        this.rabbitConnectionFactory = rabbitConnectionFactory;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    /** 애플리케이션 완전 기동 후(빈 초기화 완료) 세션들을 띄운다. */
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List<ChargingStationDto> chargers = retrieveAllChargers();
        if (chargers.isEmpty()) {
            LOGGER.warn("[proxy-eai] 등록된 충전기가 없습니다 — 세션을 기동하지 않습니다.");
            return;
        }

        LOGGER.info("[proxy-eai] 충전기 {}대 조회 완료 — 세션 기동 시작", chargers.size());
        for (ChargingStationDto cs : chargers) {
            String cpCsId = cs.getCpId() + "-" + cs.getCsId();
            try {
                declareQueues(cpCsId);
                ChargerSession session = new ChargerSession(cpCsId, props, sslSocketFactory,
                        rabbitConnectionFactory, rabbitTemplate);
                sessions.add(session);
                Thread thread = new Thread(session, "proxy-eai-" + cpCsId);
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                LOGGER.error("[proxy-eai] 세션 기동 실패 cpCsId={}: {}", cpCsId, e.getMessage(), e);
            }
        }
        LOGGER.info("[proxy-eai] 세션 {}개 기동 완료", sessions.size());
    }

    /** 등록된 충전기 전체 목록 조회 — 버전/조건 필터 없이 페이징도 없이 한 번에 (기동 시 1회 조회). */
    private List<ChargingStationDto> retrieveAllChargers() {
        ChargingStationSearchCond cond = new ChargingStationSearchCond();
        cond.setPageItemSize(Integer.MAX_VALUE);
        Page<ChargingStationDto> page = chargingStationService.retrieveChargingStationBySearchCond(cond);
        return page != null && page.getResult() != null ? page.getResult() : List.of();
    }

    private void declareQueues(String cpCsId) {
        boolean durable = props.getQueue().isDurable();
        Queue requestQueue = new Queue(props.getQueue().getRequestPrefix() + cpCsId, durable);
        Queue responseQueue = new Queue(props.getQueue().getResponsePrefix() + cpCsId, durable);
        amqpAdmin.declareQueue(requestQueue);
        amqpAdmin.declareQueue(responseQueue);
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.info("[proxy-eai] 세션 {}개 종료 중...", sessions.size());
        sessions.forEach(ChargerSession::stop);
    }
}
