package kr.co.kevit.localcsms.ocpp20.bean.req;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.ocpp201.response.DataTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OpenPeriodicEventStreamBean  implements ControlerBean {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenPeriodicEventStreamBean.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        LOGGER.debug("OpenPeriodicEventStreamBean CSID : {}", cpCsId);
        String[] csIds = cpCsId.split(StringConstants.DASH);
        return objectMapper.createObjectNode();
    }
    
}
