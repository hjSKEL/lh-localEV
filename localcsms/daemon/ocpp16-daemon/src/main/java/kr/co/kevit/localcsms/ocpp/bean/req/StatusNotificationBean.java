package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class StatusNotificationBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(StatusNotificationBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChargerStatusService chargerStatusService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String[] csIds = cpCsId.split(StringConstants.DASH);

        kr.co.kevit.ocpp16.request.StatusNotification request =
                objectMapper.treeToValue(msg.getPayload(), kr.co.kevit.ocpp16.request.StatusNotification.class);
        if (log.isDebugEnabled()) {
            log.debug("StatusNotificationBean cpCsId={} payload={}", cpCsId, msg.getPayload());
        }
        if (request.getTimestamp() == null) {
            return objectMapper.createObjectNode();
        }

        List<ChargerStatusInfo> chargerStatusInfos = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo chargerStatusInfo = chargerStatusInfos.get(0);
        if (chargerStatusInfos.size() > 1) {
            for (ChargerStatusInfo datum : chargerStatusInfos) {
                if (datum.getEvseId() == request.getConnectorId()) {
                    chargerStatusInfo = datum;
                    break;
                }
            }
        }
        if (request.getTimestamp() == null) {
            request.setTimestamp(DateUtils.dateToString(new Date(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT));
        }
        if (request.getTimestamp().contains(StringConstants.DOT)) {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT_WITH_SSS, DateUtils.UTC));
        } else {
            chargerStatusInfo.setInfoCollDate(DateUtils.stringToDate(request.getTimestamp(), DateUtils.RFC3339_DEFAULT_DATE_FORMAT, DateUtils.UTC));
        }

        switch (request.getStatus()) {
            case Available:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.STANDBY.getCode());
                chargerStatusInfo.setCsErrorStatus(StringConstants.BLANK);
                chargerStatusInfo.setCutCardNo(null);
                chargerStatusInfo.setCsCableStatus(StringConstants.ZERO);
                break;
            case Charging:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.CHARGING.getCode());
                chargerStatusInfo.setCutCardNo(null);
                break;
            case Finishing:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.COMPLETE.getCode());
                chargerStatusInfo.setCutCardNo(null);
                break;
            case Preparing:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.STANDBY4USE.getCode());
                break;
            case Reserved:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.RESERVATION.getCode());
                chargerStatusInfo.setCutCardNo(null);
                break;
            case Faulted:
            case Unavailable:
                chargerStatusInfo.setCsStatCode(ChargeStatusType.REPAIRING.getCode());
                chargerStatusInfo.setCsErrorStatus(request.getVendorErrorCode());
                chargerStatusInfo.setCutCardNo(null);
                break;
            default:
                break;
        }
        chargerStatusInfo.setEventCode(StringConstants.BLANK);
        chargerStatusInfo.setUpdateDate(new Date());
        chargerStatusService.modifyChargerStatus(chargerStatusInfo);
        return objectMapper.createObjectNode();
    }
}
