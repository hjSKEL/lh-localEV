package kr.co.kevit.localcsms.ocpp.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetCompositeScheduleBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetCompositeScheduleBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        if (log.isDebugEnabled()) {
            kr.co.kevit.ocpp16.response.GetCompositeSchedule response =
                    objectMapper.treeToValue(payload, kr.co.kevit.ocpp16.response.GetCompositeSchedule.class);
            log.debug("GetCompositeScheduleBean cpCsId={} getStatus={} getConnectorId={}",
                    cpCsId, response.getStatus(), response.getConnectorId());
            if (response.getChargingSchedule() != null) {
                log.debug("GetCompositeScheduleBean chargingRateUnit={} duration={}",
                        response.getChargingSchedule().getChargingRateUnit(),
                        response.getChargingSchedule().getDuration());
            }
        }
    }
}
