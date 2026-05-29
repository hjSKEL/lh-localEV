package kr.co.kevit.localcsms.ocpp20.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kr.co.kevit.localcsms.smartcharging.process.DynamicPushItem;
import kr.co.kevit.localcsms.smartcharging.process.DynamicScheduleManager;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp20.handler.Ocpp20WebSocketHandler;
import kr.co.kevit.ocpp201.request.UpdateDynamicSchedule;

/**
 * Dynamic 충전 프로파일 주기 push (OCPP 2.1 K28).
 *
 * <p>dynUpdateInterval 경과 프로파일을 주기적으로 조회({@link DynamicScheduleManager})하여
 * 연결된 CS 에 UpdateDynamicSchedule CALL 송신. 미연결 CS 는 건너뛰어 다음 주기 재시도.</p>
 */
@Component
public class DynamicSchedulePushScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSchedulePushScheduler.class);

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired(required = false)
    private DynamicScheduleManager dynamicScheduleManager;

    @Autowired(required = false)
    private Ocpp20WebSocketHandler webSocketHandler;

    /** 30초 주기. 각 프로파일의 dynUpdateInterval 도래 여부는 조회 쿼리에서 판단. */
    @Scheduled(fixedDelay = 30000L, initialDelay = 30000L)
    public void pushDueUpdates() {
        if (dynamicScheduleManager == null || webSocketHandler == null) {
            return;
        }
        List<DynamicPushItem> items;
        try {
            items = dynamicScheduleManager.collectDueUpdates();
        } catch (Exception e) {
            LOGGER.warn("Dynamic push 대상 조회 실패: {}", e.getMessage());
            return;
        }
        for (DynamicPushItem item : items) {
            String cpCsId = item.getCpId() + StringConstants.DASH + item.getCsId();
            if (!webSocketHandler.isConnected(cpCsId)) {
                continue; // 미연결 → 다음 주기 재시도
            }
            try {
                UpdateDynamicSchedule req = new UpdateDynamicSchedule();
                req.setChargingProfileId(item.getProfileId());
                req.setScheduleUpdate(item.getScheduleUpdate());
                ObjectNode payload = objectMapper.valueToTree(req);
                webSocketHandler.sendCommand(cpCsId, "UpdateDynamicSchedule", payload, null);
                dynamicScheduleManager.markPushed(item.getProfileId());
                LOGGER.info("UpdateDynamicSchedule push cpCsId={} profileId={}", cpCsId, item.getProfileId());
            } catch (Exception e) {
                LOGGER.warn("UpdateDynamicSchedule push 실패 cpCsId={} profileId={}: {}",
                        cpCsId, item.getProfileId(), e.getMessage());
            }
        }
    }
}
