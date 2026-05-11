package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.CsConfigurationProvider;
import kr.co.kevit.localcsms.charger.entity.dao.CsConfigurationMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsConfiguration;
import kr.co.kevit.localcsms.charger.entity.shared.CsConfigurationSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHCF001 - 충전기 설정정보 Provider 구현
 */
@Service
public class CsConfigurationProviderImpl implements CsConfigurationProvider {

    private final CsConfigurationMapper mapper;

    public CsConfigurationProviderImpl(CsConfigurationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void saveConfiguration(CsConfiguration config) {
        int updated = mapper.updateConfiguration(config);
        if (updated == 0) {
            mapper.insertConfiguration(config);
        }
    }

    @Override
    public void saveAll(String cpId, String csId, List<CsConfiguration> configs) {
        if (configs == null || configs.isEmpty()) return;
        for (CsConfiguration c : configs) {
            saveConfiguration(c);
        }
    }

    @Override
    public CsConfiguration findOne(String cpId, String csId, String configKey) {
        return mapper.findOne(cpId, csId, configKey);
    }

    @Override
    public List<CsConfiguration> findByCpIdAndCsId(String cpId, String csId) {
        return mapper.selectByCpIdAndCsId(cpId, csId);
    }

    @Override
    public Page<CsConfiguration> findBySearchCond(CsConfigurationSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<CsConfiguration> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }

    @Override
    public void deleteAllByCpIdAndCsId(String cpId, String csId) {
        mapper.deleteAllByCpIdAndCsId(cpId, csId);
    }

    @Override
    public void deleteOne(String cpId, String csId, String configKey) {
        mapper.deleteOne(cpId, csId, configKey);
    }
}
