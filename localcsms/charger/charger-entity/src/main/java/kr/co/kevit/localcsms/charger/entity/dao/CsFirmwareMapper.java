package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.entity.shared.CsFirmwareSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHFW001 - 충전기 펌웨어 업데이트 Mapper
 */
@Repository
public interface CsFirmwareMapper {

    /** 등록 */
    int insertFirmware(@Param("f") CsFirmware firmware);

    /** 상태 업데이트 */
    int updateFirmwareStatus(@Param("f") CsFirmware firmware);

    /** 전체 업데이트 */
    int updateFirmware(@Param("f") CsFirmware firmware);

    /** PK 기준 단건 조회 */
    CsFirmware selectOne(
            @Param("cpId") String cpId,
            @Param("csId") String csId);

    /** 조건 검색 (페이지 처리) */
    List<CsFirmware> selectBySearchCond(@Param("cond") CsFirmwareSearchCond cond);

    /** 조건 검색 건수 */
    int countBySearchCond(@Param("cond") CsFirmwareSearchCond cond);
}
