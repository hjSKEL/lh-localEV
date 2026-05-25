package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("GetDERControl")
public class GetDERControlBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetDERControlBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.GetDERControl response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.GetDERControl.class);
        log.info("[OCPP20] GetDERControlResponse cpCsId={} uniqueId={} status={} statusInfo={}",
                cpCsId, uniqueId, response.getStatus(),
                response.getStatusInfo() != null ? response.getStatusInfo().getReasonCode() : null);
        // ReportDERControlRequest 가 별도로 비동기 도착하므로 여기서는 status 만 기록.
    }
}
