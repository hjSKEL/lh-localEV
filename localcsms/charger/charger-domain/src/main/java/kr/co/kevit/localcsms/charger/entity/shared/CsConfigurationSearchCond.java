package kr.co.kevit.localcsms.charger.entity.shared;

import kr.co.kevit.localcsms.common.util.page.PageCriteria;

/**
 * TB_CHCF001 조회 조건
 */
public class CsConfigurationSearchCond extends PageCriteria {

    /** 충전소 ID (필수) */
    private String cpId;
    /** 충전기 ID (필수) */
    private String csId;
    /** 설정키 필터 (선택, LIKE 검색) */
    private String configKey;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }
}
