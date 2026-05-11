package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("RequestStartTransaction")
public class RequestStartTransactionBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(RequestStartTransactionBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.RequestStartTransaction response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.RequestStartTransaction.class);
        log.debug("RequestStartTransactionBean cpCsId={} status={}", cpCsId, response.getStatus());
    }
}
