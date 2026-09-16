package kr.co.kevit.localcsms.eai.proxy.mq;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kevit.localcsms.eai.proxy.session.Cs0Session;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;

/**
 * LH 모드에서 충전기별 notify 큐({@code req.<cpCsId>}, {@code ocpp16.notify} 익스체인지에
 * 라우팅키 {@code <cpCsId>.#} 로 바인딩됨)를 구독해, action 별로 분기 처리한다.
 *
 * <p>
 * {@code BootNotification} 은 {@link ChildBootNotificationTransformer} 로
 * {@code DataTransfer.req}(ChildBootNotification) 로, {@code StatusNotification} 은
 * status 가 {@link #ALLOWED_STATUSES}(Available/Charging/Faulted) 인 경우에만
 * {@link ChildStatusRptTransformer} 로 {@code DataTransfer.req}(ChildStatusRpt) 로 변환해 전송하고
 * 나머지 status 는 드롭한다. {@code StopTransaction} 은 대응하는 {@link Recharging} 1건을 조회해
 * {@link ChildTransactionRptTransformer} 로 {@code DataTransfer.req}(ChildTransactionRpt) 로
 * 변환해 CS0 연결로 전송한다. 응답은 {@link Cs0Session} 이 원래 하던 대로 로그만 남긴다. 그 외
 * action 은 현재 단계에서는 무시한다(추후 단계적으로 추가).
 * </p>
 *
 * @author bckim
 */
public class LhChargerNotifyConsumer implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LhChargerNotifyConsumer.class);

    private static final String ACTION_BOOT_NOTIFICATION = "BootNotification";
    private static final String ACTION_STATUS_NOTIFICATION = "StatusNotification";
    private static final String ACTION_STOP_TRANSACTION = "StopTransaction";

    /** StatusNotification 전송 대상 status (OCPP1.6 ChargePointStatus 표준 철자). 그 외 status 는 드롭. */
    private static final Set<String> ALLOWED_STATUSES = Set.of("Available", "Charging", "Faulted");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String cpCsId;
    private final Cs0Session cs0Session;
    private final RechargingService rechargingService;

    public LhChargerNotifyConsumer(String cpCsId, Cs0Session cs0Session, RechargingService rechargingService) {
        this.cpCsId = cpCsId;
        this.cs0Session = cs0Session;
        this.rechargingService = rechargingService;
    }

    @Override
    public void onMessage(Message message) {
        String action = extractAction(message.getMessageProperties().getReceivedRoutingKey());
        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            if (ACTION_BOOT_NOTIFICATION.equals(action)) {
                String dataTransferReq = ChildBootNotificationTransformer.transform(body);
                LOGGER.info("[proxy-eai][LH] BootNotification → DataTransfer(ChildBootNotification) 변환 cpCsId={}",
                        cpCsId);
                cs0Session.send(dataTransferReq);
            } else if (ACTION_STATUS_NOTIFICATION.equals(action)) {
                handleStatusNotification(body);
            } else if (ACTION_STOP_TRANSACTION.equals(action)) {
                handleStopTransaction(body);
            } else {
                LOGGER.debug("[proxy-eai][LH] cpCsId={} action={} 미처리(무시)", cpCsId, action);
            }
        } catch (Exception e) {
            LOGGER.error("[proxy-eai][LH] 변환 실패 cpCsId={} action={} body={}: {}", cpCsId, action, body, e.getMessage(), e);
        }
    }

    /**
     * status 가 {@link #ALLOWED_STATUSES} 에 해당할 때만 변환/전송하고, 그 외(Preparing/SuspendedEVSE/
     * SuspendedEV/Finishing/Reserved/Unavailable 등)는 로그만 남기고 드롭한다.
     */
    private void handleStatusNotification(String body) throws Exception {
        String status = extractStatus(body);
        if (!ALLOWED_STATUSES.contains(status)) {
            LOGGER.debug("[proxy-eai][LH] StatusNotification status={} 전송 대상 아님(드롭) cpCsId={}", status, cpCsId);
            return;
        }
        String dataTransferReq = ChildStatusRptTransformer.transform(body, cpCsId);
        LOGGER.info("[proxy-eai][LH] StatusNotification(status={}) → DataTransfer(ChildStatusRpt) 변환 cpCsId={}",
                status, cpCsId);
        cs0Session.send(dataTransferReq);
    }

    private static String extractStatus(String rawStatusNotificationCall) throws Exception {
        JsonNode callArray = MAPPER.readTree(rawStatusNotificationCall);
        JsonNode payload = callArray.get(3);
        JsonNode statusNode = payload.get("status");
        return statusNode != null && !statusNode.isNull() ? statusNode.asText() : "";
    }

    /**
     * StopTransaction.req 의 transactionId(=RC_ID 뒷자리)로 대응하는 {@link Recharging} 을 조회해
     * ChildTransactionRpt 로 변환 후 전송. 조회 실패 시 로그만 남기고 드롭(재시도 없음).
     */
    private void handleStopTransaction(String body) throws Exception {
        String transactionId = extractTransactionId(body);
        int dashIdx = cpCsId.indexOf('-');
        String cpId = dashIdx >= 0 ? cpCsId.substring(0, dashIdx) : cpCsId;
        String csId = dashIdx >= 0 ? cpCsId.substring(dashIdx + 1) : "";

        Recharging recharging = rechargingService.retrieveLatestRechargingByCpCsIdAndTransactionId(cpId, csId,
                transactionId);
        if (recharging == null) {
            LOGGER.warn("[proxy-eai][LH] StopTransaction 대응 Recharging 조회 실패 cpCsId={} transactionId={} — 드롭",
                    cpCsId, transactionId);
            return;
        }

        String dataTransferReq = ChildTransactionRptTransformer.transform(recharging, cpCsId);
        LOGGER.info("[proxy-eai][LH] StopTransaction → DataTransfer(ChildTransactionRpt) 변환 cpCsId={} rechargingId={}",
                cpCsId, recharging.getRechargingId());
        cs0Session.send(dataTransferReq);
    }

    private static String extractTransactionId(String rawStopTransactionCall) throws Exception {
        JsonNode callArray = MAPPER.readTree(rawStopTransactionCall);
        JsonNode payload = callArray.get(3);
        return String.valueOf(payload.get("transactionId").asInt());
    }

    /** 라우팅키 {@code <cpCsId>.<action>} 에서 {@code <action>} 부분만 추출. */
    private String extractAction(String routingKey) {
        if (routingKey == null) {
            return "";
        }
        int prefixLen = cpCsId.length() + 1;
        return routingKey.length() > prefixLen ? routingKey.substring(prefixLen) : "";
    }
}
