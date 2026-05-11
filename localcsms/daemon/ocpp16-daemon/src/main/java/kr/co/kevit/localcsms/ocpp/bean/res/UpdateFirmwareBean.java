package kr.co.kevit.localcsms.ocpp.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateFirmwareBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(UpdateFirmwareBean.class);

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("UpdateFirmwareBean cpCsId={} payload={}", cpCsId, payload);
        }
    }
}
