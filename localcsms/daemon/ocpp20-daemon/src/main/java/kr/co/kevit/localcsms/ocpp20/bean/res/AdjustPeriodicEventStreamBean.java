package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("AdjustPeriodicEventStream")
public class AdjustPeriodicEventStreamBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(AdjustPeriodicEventStreamBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.AdjustPeriodicEventStream response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.AdjustPeriodicEventStream.class);
        log.debug("AdjustPeriodicEventStreamBean cpCsId={} status={}", cpCsId, response.getStatus());
    }
}
