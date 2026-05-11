/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 *
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2023. 11. 9.
 */
public class CsCertSearchCond extends PageCriteria {

    /** 충전기 ID (CS_ID) */
    private String csId;

    /** 인증서 상태 (CT_ST) */
    private String certStatus;

    /** CA 유형 R/S (CA_TP) */
    private String caType;

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getCertStatus() { return certStatus; }
    public void setCertStatus(String certStatus) { this.certStatus = certStatus; }

    public String getCaType() { return caType; }
    public void setCaType(String caType) { this.caType = caType; }
}
