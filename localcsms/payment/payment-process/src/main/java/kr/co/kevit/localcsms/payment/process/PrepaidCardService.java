/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.process;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCardHis;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardHisSearchCond;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;

/**
 * 선불카드 서비스
 *
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface PrepaidCardService {

    /**
     * 선불카드 발급.
     * card.balance > 0 이면 ISSUE 거래이력 1건이 함께 기록된다.
     */
    void issuePrepaidCard(PrepaidCard card);

    /**
     * 충전요금 차감 (USE).
     * - 행 잠금 후 잔액·상태 검증
     * - 잔액 < 차감액 이면 예외
     * - USE 거래이력 기록 후 마스터 BALANCE 갱신
     *
     * @param cardNo       선불카드번호
     * @param amount       차감 금액 (양수)
     * @param rechargingId 연결 충전ID (필수)
     * @param updUserId    조작자 ID
     * @return 기록된 거래이력
     */
    PrepaidCardHis usePrepaidCard(String cardNo, Long amount, String rechargingId, String updUserId);

    /**
     * 선불카드 상태 변경 (활성/정지/만료).
     * 잔액·고객·만료일은 건드리지 않는다.
     */
    void modifyCardStatus(String cardNo, String cardStatCode, String updUserId);

    PrepaidCard retrievePrepaidCard(String cardNo);

    Page<PrepaidCardDto> retrievePrepaidCardBySearchCond(PrepaidCardSearchCond searchCond);

    Page<PrepaidCardHisDto> retrievePrepaidCardHisBySearchCond(PrepaidCardHisSearchCond searchCond);

}
