package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("GetTransactionStatus")
public class GetTransactionStatusBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetTransactionStatusBean.class);

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        log.debug("GetTransactionStatusBean cpCsId={} payload={}", cpCsId, payload);
    }
}
