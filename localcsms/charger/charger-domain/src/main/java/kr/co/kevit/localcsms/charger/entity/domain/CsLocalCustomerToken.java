package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_CHLC002 - 충전기 로컬고객명단 (Local Authorization Token Item)
 *
 * 충전기별 로컬 인증 목록의 개별 토큰 항목.
 */
public class CsLocalCustomerToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 충전소 ID  CP_ID VARCHAR(6) */
    private String cpId;

    /** 충전기 ID  CS_ID VARCHAR(2) */
    private String csId;

    /** 인증 토큰 값  ID_TOKEN VARCHAR(36) */
    private String token;

    /** 토큰 타입 (ISO14443/eMAID/EVCCID/Local/...)  ID_TOKEN_TP VARCHAR(30) */
    private String tokenType;

    /** 인증 상태 (Accepted/Blocked/Expired/Invalid/...)  TOKEN_STATUS VARCHAR(30) */
    private String tokenStatus;

    /** 캐시 만료일시  CACHE_EXP_DT DATETIME */
    private Date cacheExpireDate;

    /** 부모(그룹) 토큰 값  PRNT_ID_TOKEN VARCHAR(36) */
    private String parentIdToken;

    /** 부모(그룹) 토큰 타입  PRNT_TOKEN_TP VARCHAR(30) */
    private String parentTokenType;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getTokenStatus() { return tokenStatus; }
    public void setTokenStatus(String tokenStatus) { this.tokenStatus = tokenStatus; }

    public Date getCacheExpireDate() { return cacheExpireDate; }
    public void setCacheExpireDate(Date cacheExpireDate) { this.cacheExpireDate = cacheExpireDate; }

    public String getParentIdToken() { return parentIdToken; }
    public void setParentIdToken(String parentIdToken) { this.parentIdToken = parentIdToken; }

    public String getParentTokenType() { return parentTokenType; }
    public void setParentTokenType(String parentTokenType) { this.parentTokenType = parentTokenType; }
}
