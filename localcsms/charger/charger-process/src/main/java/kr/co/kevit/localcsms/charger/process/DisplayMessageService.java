package kr.co.kevit.localcsms.charger.process;

import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessage;
import kr.co.kevit.localcsms.charger.entity.domain.DisplayMessageContent;
import kr.co.kevit.localcsms.charger.entity.shared.DisplayMessageSearchCond;
import kr.co.kevit.localcsms.common.util.page.Page;

import java.util.List;

/**
 * TB_CHDM001 / TB_CHDM002 - 충전기 디스플레이 메시지 Service
 */
public interface DisplayMessageService {

    /** 메시지 저장 (insert, 생성된 ID 반영) */
    void saveMessage(DisplayMessage message);

    /** 메시지 수정 */
    void updateMessage(DisplayMessage message);

    /** 메시지 + 내용 일괄 저장 */
    void saveMessageWithContents(DisplayMessage message);

    /** 메시지 단건 삭제 (내용 포함) */
    void deleteMessage(int messageId, String cpId, String csId);

    /** 충전기 메시지 전체 삭제 */
    void deleteAllByCpIdAndCsId(String cpId, String csId);

    /** 단건 조회 */
    DisplayMessage findOne(int messageId, String cpId, String csId);

    /** 충전기 전체 메시지 조회 (내용 포함) */
    List<DisplayMessage> retrieveByCpIdAndCsId(String cpId, String csId);

    /** 조건 검색 (페이지 처리) */
    Page<DisplayMessage> retrieveBySearchCond(DisplayMessageSearchCond cond);

    /** 메시지 내용 upsert */
    void saveContent(DisplayMessageContent content);
}
