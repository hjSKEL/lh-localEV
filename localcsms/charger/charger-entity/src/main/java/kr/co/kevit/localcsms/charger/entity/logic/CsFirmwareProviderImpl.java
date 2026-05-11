package kr.co.kevit.localcsms.charger.entity.logic;

import kr.co.kevit.localcsms.charger.entity.CsFirmwareProvider;
import kr.co.kevit.localcsms.charger.entity.dao.CsFirmwareMapper;
import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_CHFW001 - 충전기 펌웨어 Provider 구현
 */
@Service
public class CsFirmwareProviderImpl implements CsFirmwareProvider {

    private final CsFirmwareMapper mapper;

    public CsFirmwareProviderImpl(CsFirmwareMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Page<CsFirmware> findBySearchCond(CsFirmwareSearchCond cond) {
        int total = mapper.countBySearchCond(cond);
        cond.setTotalItemCount(total);
        List<CsFirmware> list = total > 0 ? mapper.selectBySearchCond(cond) : new ArrayList<>();
        return new Page<>(cond, list);
    }

    @Override
    public void updateFirmwareStatus(CsFirmware firmware) {
        mapper.updateFirmwareStatus(firmware);
    }

    @Override
    public void updateFirmware(CsFirmware firmware) {
        mapper.updateFirmware(firmware);
    }

    @Override
    public int updateFirmwareAll(CsFirmware firmware) {
        return mapper.updateFirmware(firmware);
    }

    @Override
    public void insertFirmware(CsFirmware firmware) {
        mapper.insertFirmware(firmware);
    }
}
