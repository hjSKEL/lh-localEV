package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.CsConfigurationProvider;
import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import kr.co.kevit.localcsms.charger.process.CsConfigurationService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TB_CHCF001 - 충전기 설정정보 Service 구현
 */
@Service
public class CsConfigurationServiceImpl implements CsConfigurationService {

    private final CsConfigurationProvider csConfigurationProvider;

    public CsConfigurationServiceImpl(CsConfigurationProvider csConfigurationProvider) {
        this.csConfigurationProvider = csConfigurationProvider;
    }

    @Override
    public void saveConfiguration(CsConfiguration config) {
        csConfigurationProvider.saveConfiguration(config);
    }

    @Override
    public void saveAll(String cpId, String csId, List<CsConfiguration> configs) {
        csConfigurationProvider.saveAll(cpId, csId, configs);
    }

    @Override
    public CsConfiguration findOne(String cpId, String csId, String configKey) {
        return csConfigurationProvider.findOne(cpId, csId, configKey);
    }

    @Override
    public List<CsConfiguration> retrieveByCpIdAndCsId(String cpId, String csId) {
        return csConfigurationProvider.findByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<CsConfiguration> retrieveBySearchCond(CsConfigurationSearchCond cond) {
        return csConfigurationProvider.findBySearchCond(cond);
    }

    @Override
    public void deleteAllByCpIdAndCsId(String cpId, String csId) {
        csConfigurationProvider.deleteAllByCpIdAndCsId(cpId, csId);
    }

    @Override
    public void deleteOne(String cpId, String csId, String configKey) {
        csConfigurationProvider.deleteOne(cpId, csId, configKey);
    }
}
