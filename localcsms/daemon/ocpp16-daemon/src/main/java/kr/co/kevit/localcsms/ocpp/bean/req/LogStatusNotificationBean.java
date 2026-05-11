package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogStatusNotificationBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(LogStatusNotificationBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (log.isDebugEnabled()) {
            kr.co.kevit.ocpp16.request.LogStatusNotification request =
                    objectMapper.treeToValue(msg.getPayload(), kr.co.kevit.ocpp16.request.LogStatusNotification.class);
            log.debug("LogStatusNotificationBean cpCsId={} getRequestId={} getStatus={}", cpCsId, request.getRequestId(), request.getStatus());
        }
        return objectMapper.createObjectNode();
    }
}
