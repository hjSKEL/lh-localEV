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

@Component("NotifySettlement")
public class NotifySettlementBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifySettlementBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String[] csIds = cpCsId.split(StringConstants.DASH);
        String cpId = csIds.length > 0 ? csIds[0] : null;
        String csId = csIds.length > 1 ? csIds[1] : null;

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.NotifySettlement request = objectMapper.readValue(text,
                kr.co.kevit.ocpp201.request.NotifySettlement.class);
        kr.co.kevit.ocpp201.response.NotifySettlement response = new kr.co.kevit.ocpp201.response.NotifySettlement();
        response.setReceiptId(request.getReceiptId());
        response.setReceiptUrl("https://www.kevit.co.kr/receipt/" + cpId + "/" + csId);
        return objectMapper.valueToTree(response);
    }

}
