/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity;

import kr.co.kevit.localcsms.common.util.page.Page;
import kr.co.kevit.localcsms.payment.entity.domain.PrepaidCard;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardDto;
import kr.co.kevit.localcsms.payment.entity.shared.PrepaidCardSearchCond;

/**
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2026. 5. 13.
 */
public interface PrepaidCardProvider {

    void registerPrepaidCard(PrepaidCard card);

    void modifyPrepaidCard(PrepaidCard card);

    /**
     * 잔액 변경 (낙관적 검증).
     * @return 영향 행 수 — 0 이면 동시성 충돌 또는 잔액 변동
     */
    int modifyBalance(String cardNo, Long expectedBalance, Long newBalance, String updUserId);

    /** 상태 코드만 변경 (활성/정지/만료 토글). */
    int modifyCardStatus(String cardNo, String cardStatCode, String updUserId);

    PrepaidCard retrievePrepaidCard(String cardNo);

    /** SELECT FOR UPDATE — 트랜잭션 내부에서만 호출 */
    PrepaidCard retrievePrepaidCardForUpdate(String cardNo);

    Page<PrepaidCardDto> retrievePrepaidCardBySearchCond(PrepaidCardSearchCond searchCond);

}
