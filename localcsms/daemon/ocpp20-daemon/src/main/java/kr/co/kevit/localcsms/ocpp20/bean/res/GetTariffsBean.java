package kr.co.kevit.localcsms.ocpp20.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.ocpp20.bean.ResponderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("GetTariffs")
public class GetTariffsBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetTariffsBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp201.response.GetTariffs response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp201.response.GetTariffs.class);
        log.info("[OCPP20] GetTariffsResponse cpCsId={} uniqueId={} status={} assignments={}",
                cpCsId, uniqueId, response.getStatus(),
                response.getTariffAssignments() != null ? response.getTariffAssignments().size() : 0);
    }
}
