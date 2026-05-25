/*******************************************************************************
 * Copyright(c) 2026 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.payment.entity.shared;

import kr.co.kevit.localcsms.payment.entity.domain.TariffAssignment;

public class TariffAssignmentDto extends TariffAssignment {
    private static final long serialVersionUID = 5301025001801251213L;

    /** Tariff master 의 currency / kind / description — join 결과 */
    private String tariffCurrency;
    private String tariffKind;
    private String tariffDescription;

    public String getTariffCurrency() { return tariffCurrency; }
    public void setTariffCurrency(String tariffCurrency) { this.tariffCurrency = tariffCurrency; }
    public String getTariffKind() { return tariffKind; }
    public void setTariffKind(String tariffKind) { this.tariffKind = tariffKind; }
    public String getTariffDescription() { return tariffDescription; }
    public void setTariffDescription(String tariffDescription) { this.tariffDescription = tariffDescription; }
}
