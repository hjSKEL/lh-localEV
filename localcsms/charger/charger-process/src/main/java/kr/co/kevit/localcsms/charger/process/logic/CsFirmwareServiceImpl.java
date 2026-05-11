package kr.co.kevit.localcsms.charger.process.logic;

import kr.co.kevit.localcsms.charger.entity.CsFirmwareProvider;
import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.common.util.page.Page;
import org.springframework.stereotype.Service;

/**
 * TB_CHFW001 - 충전기 펌웨어 Service 구현
 */
@Service
public class CsFirmwareServiceImpl implements CsFirmwareService {

    private final CsFirmwareProvider csFirmwareProvider;

    public CsFirmwareServiceImpl(CsFirmwareProvider csFirmwareProvider) {
        this.csFirmwareProvider = csFirmwareProvider;
    }

    @Override
    public Page<CsFirmware> retrieveBySearchCond(CsFirmwareSearchCond cond) {
        return csFirmwareProvider.findBySearchCond(cond);
    }

    @Override
    public void modifyFirmwareStatus(CsFirmware firmware) {
        csFirmwareProvider.updateFirmwareStatus(firmware);
    }

    @Override
    public void modifyFirmware(CsFirmware firmware) {
        csFirmwareProvider.updateFirmware(firmware);
    }

    @Override
    public void saveFirmware(CsFirmware firmware) {
        java.util.Date now = new java.util.Date();
        firmware.setRequestDate(now);
        firmware.setUpdateDate(now);
        int updated = csFirmwareProvider.updateFirmwareAll(firmware);
        if (updated == 0) {
            csFirmwareProvider.insertFirmware(firmware);
        }
    }
}
