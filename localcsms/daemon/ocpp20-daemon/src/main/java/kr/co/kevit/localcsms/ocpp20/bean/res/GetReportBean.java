package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("GetReport")
public class GetReportBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetReportBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.GetReport response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.GetReport.class);
        log.debug("GetReportBean cpCsId={} status={}", cpCsId, response.getStatus());
    }
}
