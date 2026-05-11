package kr.co.kevit.localcsms.ocpp.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.charger.entity.domain.ChargerStatusInfo;
import kr.co.kevit.localcsms.charger.process.ChargerStatusInfoHisService;
import kr.co.kevit.localcsms.charger.process.ChargerStatusService;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import kr.co.kevit.ocpp16.enumtype.ARStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RemoteStartTransactionBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(RemoteStartTransactionBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private ChargerStatusService chargerStatusService;
    @Autowired private ChargerStatusInfoHisService statusHisService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp16.response.RemoteStartTransaction response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp16.response.RemoteStartTransaction.class);
        log.debug("RemoteStartTransactionBean cpCsId={} getStatus={}", cpCsId, response.getStatus());
        String[] csIds = cpCsId.split(StringConstants.DASH);
        List<ChargerStatusInfo> status = chargerStatusService.retrieveChargerStatusByCpIdNCsId(csIds[0], csIds[1]);
        ChargerStatusInfo datum = status.get(0);
        datum.setEvseId(1);
        datum.setInfoCollDate(new Date());
        datum.setEventCode(response.getStatus() == ARStatusEnum.Accepted ? "EVTR01" : "EVTR02");
        statusHisService.registerChargerStatusHis(datum);
    }
}
