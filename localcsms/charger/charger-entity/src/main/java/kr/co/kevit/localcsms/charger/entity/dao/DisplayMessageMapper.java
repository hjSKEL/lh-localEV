package kr.co.kevit.localcsms.charger.entity.dao;

import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHDM001 / TB_CHDM002 - 충전기 디스플레이 메시지 Mapper
 */
@Repository
public interface DisplayMessageMapper {

    /** TB_CHDM001 단건 INSERT */
    int insertMessage(@Param("m") DisplayMessage message);

    /** TB_CHDM001 단건 UPDATE */
    int updateMessage(@Param("m") DisplayMessage message);

    /** TB_CHDM001 단건 DELETE */
    int deleteMessage(@Param("messageId") int messageId,
                      @Param("cpId") String cpId,
                      @Param("csId") String csId);

    /** TB_CHDM001 충전기 전체 삭제 */
    int deleteAllByCpIdAndCsId(@Param("cpId") String cpId,
                               @Param("csId") String csId);

    /** TB_CHDM001 단건 조회 (contents 미포함) */
    DisplayMessage findOne(@Param("messageId") int messageId,
                           @Param("cpId") String cpId,
                           @Param("csId") String csId);

    /** TB_CHDM001 충전기 전체 목록 조회 (contents 포함) */
    List<DisplayMessage> selectByCpIdAndCsId(@Param("cpId") String cpId,
                                             @Param("csId") String csId);

    /** TB_CHDM001 조건 검색 (Page 처리) */
    List<DisplayMessage> selectBySearchCond(@Param("cond") DisplayMessageSearchCond cond);

    int countBySearchCond(@Param("cond") DisplayMessageSearchCond cond);

    /** TB_CHDM002 내용 INSERT */
    int insertContent(@Param("c") DisplayMessageContent content);

    /** TB_CHDM002 내용 UPSERT */
    int mergeContent(@Param("c") DisplayMessageContent content);

    /** TB_CHDM002 메시지 내용 전체 삭제 */
    int deleteContents(@Param("messageId") int messageId);

    /** TB_CHDM002 내용 목록 조회 */
    List<DisplayMessageContent> selectContents(@Param("messageId") int messageId);
}
