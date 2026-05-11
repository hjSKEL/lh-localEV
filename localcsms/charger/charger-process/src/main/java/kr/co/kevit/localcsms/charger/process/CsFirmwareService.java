package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

/**
 * TB_CHFW001 - 충전기 펌웨어 Service
 */
public interface CsFirmwareService {

    /** 조건 검색 (페이지 처리) */
    Page<CsFirmware> retrieveBySearchCond(CsFirmwareSearchCond cond);

    /** 펌웨어 상태 업데이트 */
    void modifyFirmwareStatus(CsFirmware firmware);

    /** 펌웨어 전체 업데이트 */
    void modifyFirmware(CsFirmware firmware);

    /** 펌웨어 저장 (update 후 0건이면 insert) */
    void saveFirmware(CsFirmware firmware);
}
