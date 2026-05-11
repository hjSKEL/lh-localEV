package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CHCF001
 * OCPP 1.6 GetConfiguration 결과 - 충전기 설정정보
 */
public class CsConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 충전소 ID  CP_ID CHAR(6) */
    private String cpId;
    /** PK: 충전기 ID  CS_ID CHAR(2) */
    private String csId;
    /** PK: 설정키  CONF_KEY VARCHAR(50) */
    private String configKey;
    /** 설정값  CONF_VAL VARCHAR(500) */
    private String configValue;
    /** 읽기전용여부  READONLY_YN CHAR(1) DEFAULT 'Y' */
    private String readonlyYn = "Y";
    /** 최종수정일시  UPD_DT DATETIME */
    private Date updateDate;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getReadonlyYn() { return readonlyYn; }
    public void setReadonlyYn(String readonlyYn) { this.readonlyYn = readonlyYn; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
