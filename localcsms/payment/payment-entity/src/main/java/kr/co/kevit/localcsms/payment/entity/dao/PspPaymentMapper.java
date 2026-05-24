/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.kevit.localcsms.payment.entity.domain.PspPayment;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentDto;
import kr.co.kevit.localcsms.payment.entity.shared.PspPaymentSearchCond;

/**
 * PSP 결제 마스터 Mapper
 *
 * @author bckim
 * @since 2026. 5. 25.
 */
@Repository
public interface PspPaymentMapper {

    int insertPspPayment(@Param("payment") PspPayment payment);

    int updatePspPayment(@Param("payment") PspPayment payment);

    int updateStatus(@Param("pspRef") String pspRef,
                     @Param("statusCd") String statusCd,
                     @Param("reasonCd") String reasonCd,
                     @Param("updUserId") String updUserId);

    int updateLinkRechargingId(@Param("pspRef") String pspRef,
                               @Param("rechargingId") String rechargingId,
                               @Param("startedDate") Date startedDate,
                               @Param("updUserId") String updUserId);

    int updateSettle(@Param("pspRef") String pspRef,
                     @Param("settledAmount") BigDecimal settledAmount,
                     @Param("receiptUrl") String receiptUrl,
                     @Param("receiptId") String receiptId,
                     @Param("settledDate") Date settledDate,
                     @Param("updUserId") String updUserId);

    int updateCancel(@Param("pspRef") String pspRef,
                     @Param("reasonCd") String reasonCd,
                     @Param("canceledDate") Date canceledDate,
                     @Param("updUserId") String updUserId);

    PspPayment selectPspPayment(@Param("pspRef") String pspRef);

    PspPayment selectPspPaymentByRechargingId(@Param("rechargingId") String rechargingId);

    int countPspPaymentBySearchCond(@Param("searchCond") PspPaymentSearchCond searchCond);

    List<PspPaymentDto> selectPspPaymentBySearchCond(@Param("searchCond") PspPaymentSearchCond searchCond);

    Integer selectDailySequence(@Param("datePrefix") String datePrefix);
}
