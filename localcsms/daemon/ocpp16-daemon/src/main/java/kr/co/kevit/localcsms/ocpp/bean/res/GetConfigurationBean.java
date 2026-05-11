package kr.co.kevit.localcsms.ocpp.bean.res;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.process.CsConfigurationService;
import kr.co.kevit.localcsms.ocpp.bean.ResponderBean;
import kr.co.kevit.ocpp16.domain.ConfigurationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetConfigurationBean implements ResponderBean {

    private static final Logger log = LoggerFactory.getLogger(GetConfigurationBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CsConfigurationService csConfigurationService;

    @Override
    public void handle(String cpCsId, JsonNode payload, String uniqueId) throws Exception {
        kr.co.kevit.ocpp16.response.GetConfiguration response =
                objectMapper.treeToValue(payload, kr.co.kevit.ocpp16.response.GetConfiguration.class);

        if (response.getUnknownKey() != null) {
            for (String value : response.getUnknownKey()) {
                log.debug("[GetConfiguration] cpCsId={} unknownKey={}", cpCsId, value);
            }
        }

        if (response.getConfigurationKey() == null || response.getConfigurationKey().isEmpty()) {
            log.debug("[GetConfiguration] cpCsId={} configurationKey is empty", cpCsId);
            return;
        }

        int idx = cpCsId.lastIndexOf('-');
        if (idx < 0) {
            log.warn("[GetConfiguration] cpCsId 파싱 실패: {}", cpCsId);
            return;
        }
        String cpId = cpCsId.substring(0, idx);
        String csId = cpCsId.substring(idx + 1);

        List<CsConfiguration> configs = new ArrayList<>();
        for (ConfigurationKey kv : response.getConfigurationKey()) {
            log.debug("[GetConfiguration] cpCsId={} key={} value={} readonly={}",
                    cpCsId, kv.getKey(), kv.getValue(), kv.isReadonly());

            CsConfiguration config = new CsConfiguration();
            config.setCpId(cpId);
            config.setCsId(csId);
            config.setConfigKey(kv.getKey());
            config.setConfigValue(kv.getValue());
            config.setReadonlyYn(kv.isReadonly() ? "Y" : "N");
            configs.add(config);
        }

        try {
            csConfigurationService.saveAll(cpId, csId, configs);
            log.info("[GetConfiguration] 설정정보 저장 완료: cpCsId={} count={}", cpCsId, configs.size());
        } catch (Exception e) {
            log.error("[GetConfiguration] 설정정보 저장 실패: cpCsId={} error={}", cpCsId, e.getMessage(), e);
        }
    }
}
