package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.RechargingStatus;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.localcsms.recharger.entity.domain.Recharging;
import kr.co.kevit.localcsms.recharger.process.RechargingService;
import kr.co.kevit.ocpp16.response.Heartbeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class HeartbeatBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String EVT000 = "EVT000";

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Autowired
    private RechargingService rechargingService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        for (ChargerStatusInfo chargerStatusInfo : chargerStatusInfos) {
            if (!StringUtils.isEmpty(chargerStatusInfo.getRechargingId()) && chargerStatusInfo.getRechargingId().length() == 24) {
                Recharging rc = rechargingService.retrieveRecharging4IfById(chargerStatusInfo.getRechargingId());
                if (rc != null && (RechargingStatus.RECS04.getCode().equals(rc.getChStatCode())
                        || RechargingStatus.RECS05.getCode().equals(rc.getChStatCode()))) {
                    chargerStatusInfo.setCuEleEnerge(BigDecimal.ZERO);
                    chargerStatusInfo.setCutCardNo(null);
                    chargerStatusInfo.setRechargingId(null);
                    chargerStatusInfo.setInstChSum(BigDecimal.ZERO);   // 순간충전금액
                    chargerStatusInfo.setInstChAmont(BigDecimal.ZERO); // 순간 충전량
                    chargerStatusInfo.setInstChCost(BigDecimal.ZERO);  // 순간충전단가
                    chargerStatusInfo.setChSum(BigDecimal.ZERO);       // 충전금액
                    chargerStatusInfo.setChStartDate(null);            // 충전시작시간
                    chargerStatusInfo.setChEndDate(null);              // 충전종료시간
                }
            }
            chargerStatusInfo.setInfoCollDate(new Date());
            chargerStatusInfo.setEventCode(EVT000);
            chargerStatusInfo.setUpdateDate(new Date());
            chargerStatusService.modifyChargerStatusWithoutHis(chargerStatusInfo);
        }

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setCurrentTime(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        if (log.isDebugEnabled()) {
            log.debug("HeartbeatBean Response : {}", objectMapper.writeValueAsString(heartbeat));
        }
        return objectMapper.valueToTree(heartbeat);
    }
}
