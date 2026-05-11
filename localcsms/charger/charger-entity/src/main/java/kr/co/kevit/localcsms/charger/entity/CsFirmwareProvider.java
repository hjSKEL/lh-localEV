package kr.co.kevit.localcsms.charger.entity;

import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * TB_CHFW001 - 충전기 펌웨어 Provider
 */
public interface CsFirmwareProvider {

    Page<CsFirmware> findBySearchCond(CsFirmwareSearchCond cond);

    void updateFirmwareStatus(CsFirmware firmware);

    void updateFirmware(CsFirmware firmware);

    int updateFirmwareAll(CsFirmware firmware);

    void insertFirmware(CsFirmware firmware);
}
