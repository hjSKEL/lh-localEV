/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.util.List;

import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringConstants;

/**
 * TB_CHCS001
 * 충전기
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 30.
 */
public class ChargingStation implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -3997536702701966750L;

    /**
     * PK
     * 충전소 ID CP_ID VARCHAR2(6 BYTE) NOT NULL,
     */
    private String cpId;

    /**
     * PK
     * 충전기 ID CS_ID VARCHAR2(2 BYTE) NOT NULL,
     */
    private String csId;

    /**
     * 충전기 고유 ID
     * CS_UNIQ_ID VARCHAR(10) NOT NULL,
     */
    private String csUniqId;

    /**
     * 충전기 채널 수
     * CS_CHN_CNT NUMBER(3)
     */
    private int csChanelCount = 1;

    /**
     * 전력량
     * ELEC_SPLY_CPTY INT(5)
     */
    private int electSupplyCapability;

    /**
     * 충전기 타입 공통코드 : CHRA00 CS_CAT_CD VARCHAR2(6 BYTE) NOT NULL,
     */
    private String csCatCode;

    /**
     * 사용여부 Y/N US_YN VARCHAR2(1 BYTE) DEFAULT 'Y',
     */
    private String useYn = StringConstants.Y;

    /**
     * 고장여부 Y/N BD_YN VARCHAR2(1 BYTE) DEFAULT 'N',
     */
    private String brkdownYn = StringConstants.N;

    /**
     * 제조사 코드
     * CHMK00
     * MAKER_TP VARCHAR2(6)
     *
     */
    private String makerType;

    /**
     * 설치년월 INS_YR_MO CHAR(6 BYTE),
     */
    private String insYearMon;

    /**
     * 충전기 설치업체(환경부보조금) CS_INS_CO VARCHAR2(50 BYTE),
     */
    private String csInstallCo;

    /**
     * 상품 타입 PO 상품, HI/LO 고압/저압, 01 순번 6자리 PRD_TP
     */
    private String prodType;

    /**
     * CS_PWD
     * VARCHAR(16)
     */
    private String csPassword;

    /**
     * LST_CS_PWD
     * VARCHAR(16)
     */
    private String lastCsPassword;

    /**
     * 충전기 타입
     * CS_KN_TP
     * CHKT00(급속,중속,완속)
     */
    private String csKindType;

    /**
     * OCPP Version
     * OCPP_VER VARCHAR(10) OCPP1.6 / OCPP2.0.1
     */
    private String ocppVersion = StringConstants.OCPP16;

    /**
     * 등록정보
     */
    private Writer writer;

    /**
     * Object Relation 충전기 실 상태 정보
     */
    private List<ChargerStatusInfo> chargerStatusInfo;

    /**
     * Object Relation
     */
    private ChargePoint chargePoint;

    /**
     * Get cpId
     * 
     * @return cpId
     */
    public String getCpId() {
        return cpId;
    }

    /**
     * Set cpId
     * 
     * @param cpId
     */
    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    /**
     * Get csId
     * 
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * 
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get csUniqId
     * 
     * @return csUniqId
     */
    public String getCsUniqId() {
        return csUniqId;
    }

    /**
     * Set csUniqId
     * 
     * @param csUniqId
     */
    public void setCsUniqId(String csUniqId) {
        this.csUniqId = csUniqId;
    }

    /**
     * Get csChanelCount
     * 
     * @return csChanelCount
     */
    public int getCsChanelCount() {
        return csChanelCount;
    }

    /**
     * Set csChanelCount
     * 
     * @param csChanelCount
     */
    public void setCsChanelCount(int csChanelCount) {
        this.csChanelCount = csChanelCount;
    }

    /**
     * Get electSupplyCapability
     * 
     * @return electSupplyCapability
     */
    public int getElectSupplyCapability() {
        return electSupplyCapability;
    }

    /**
     * Set electSupplyCapability
     * 
     * @param electSupplyCapability
     */
    public void setElectSupplyCapability(int electSupplyCapability) {
        this.electSupplyCapability = electSupplyCapability;
    }

    /**
     * Get csCatCode
     * 
     * @return csCatCode
     */
    public String getCsCatCode() {
        return csCatCode;
    }

    /**
     * Set csCatCode
     * 
     * @param csCatCode
     */
    public void setCsCatCode(String csCatCode) {
        this.csCatCode = csCatCode;
    }

    /**
     * Get useYn
     * 
     * @return useYn
     */
    public String getUseYn() {
        return useYn;
    }

    /**
     * Set useYn
     * 
     * @param useYn
     */
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    /**
     * Get brkdownYn
     * 
     * @return brkdownYn
     */
    public String getBrkdownYn() {
        return brkdownYn;
    }

    /**
     * Set brkdownYn
     * 
     * @param brkdownYn
     */
    public void setBrkdownYn(String brkdownYn) {
        this.brkdownYn = brkdownYn;
    }

    /**
     * Get makerType
     * 
     * @return makerType
     */
    public String getMakerType() {
        return makerType;
    }

    /**
     * Set makerType
     * 
     * @param makerType
     */
    public void setMakerType(String makerType) {
        this.makerType = makerType;
    }

    /**
     * Get insYearMon
     * 
     * @return insYearMon
     */
    public String getInsYearMon() {
        return insYearMon;
    }

    /**
     * Set insYearMon
     * 
     * @param insYearMon
     */
    public void setInsYearMon(String insYearMon) {
        this.insYearMon = insYearMon;
    }

    /**
     * Get csInstallCo
     * 
     * @return csInstallCo
     */
    public String getCsInstallCo() {
        return csInstallCo;
    }

    /**
     * Set csInstallCo
     * 
     * @param csInstallCo
     */
    public void setCsInstallCo(String csInstallCo) {
        this.csInstallCo = csInstallCo;
    }

    /**
     * Get prodType
     * 
     * @return prodType
     */
    public String getProdType() {
        return prodType;
    }

    /**
     * Set prodType
     * 
     * @param prodType
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    /**
     * Get csPassword
     * 
     * @return csPassword
     */
    public String getCsPassword() {
        return csPassword;
    }

    /**
     * Set csPassword
     *
     * @param csPassword
     */
    public void setCsPassword(String csPassword) {
        this.csPassword = csPassword;
    }

    /**
     * Get lastCsPassword
     *
     * @return lastCsPassword
     */
    public String getLastCsPassword() {
        return lastCsPassword;
    }

    /**
     * Set lastCsPassword
     *
     * @param lastCsPassword
     */
    public void setLastCsPassword(String lastCsPassword) {
        this.lastCsPassword = lastCsPassword;
    }

    /**
     * Get csKindType
     * 
     * @return csKindType
     */
    public String getCsKindType() {
        return csKindType;
    }

    /**
     * Set csKindType
     * 
     * @param csKindType
     */
    public void setCsKindType(String csKindType) {
        this.csKindType = csKindType;
    }

    /**
     * Get writer
     * 
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * 
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Get chargerStatusInfo
     * 
     * @return chargerStatusInfo
     */
    public List<ChargerStatusInfo> getChargerStatusInfo() {
        return chargerStatusInfo;
    }

    /**
     * Set chargerStatusInfo
     * 
     * @param chargerStatusInfo
     */
    public void setChargerStatusInfo(List<ChargerStatusInfo> chargerStatusInfo) {
        this.chargerStatusInfo = chargerStatusInfo;
    }

    /**
     * Get chargePoint
     * 
     * @return chargePoint
     */
    public ChargePoint getChargePoint() {
        return chargePoint;
    }

    /**
     * Set chargePoint
     * 
     * @param chargePoint
     */
    public void setChargePoint(ChargePoint chargePoint) {
        this.chargePoint = chargePoint;
    }

    /**
     * Get ocppVersion
     * 
     * @return ocppVersion
     */
    public String getOcppVersion() {
        return ocppVersion;
    }

    /**
     * Set ocppVersion
     * 
     * @param ocppVersion
     */
    public void setOcppVersion(String ocppVersion) {
        this.ocppVersion = ocppVersion;
    }

}
