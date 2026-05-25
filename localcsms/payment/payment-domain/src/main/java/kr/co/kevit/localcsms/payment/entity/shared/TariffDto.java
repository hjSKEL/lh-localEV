/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.Tariff;

public class TariffDto extends Tariff {
    private static final long serialVersionUID = 5301025001801251211L;

    /** UI 표시용 — 활성 매핑 수 */
    private Integer activeAssignmentCount;

    public Integer getActiveAssignmentCount() { return activeAssignmentCount; }
    public void setActiveAssignmentCount(Integer activeAssignmentCount) { this.activeAssignmentCount = activeAssignmentCount; }
}
