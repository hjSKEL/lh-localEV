/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * TB_CACS001
 * @author KEVIT <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 11. 9.
 */
public class CsCert implements Serializable{
    
    /**  */
    private static final long serialVersionUID = -6281964746675084459L;

    /**
     * CT_ID CHAR(17)
     * ex)20231109170310123
     */
    private BigInteger certId;
    
    /**
     * CS_ID VARCHAR(14)
     * 10000000101
     */
    private String csId;
    
    /**
     * CM_NM VARCHAR(250)
     */
    private String CommonName;
    
    /**
     * FILE_LOC VARCHAR(50)
     */
    private String fileLocation;
    
    /**
     * CT_ST CHAR(6)
     * 공통코드 CAST00
     */
    private String certStatus;
    
    /**
     * EXP_DT DATETIME
     * 만료일시
     */
    private Date expiredDate;
    
    /**
     * CA_TP CHAR(1)
     * R : ROOT
     * S : SubCA
     */
    private String caType;
    
    /**
     * 
     */
    private Writer writer;

    /**
     * Get certId
     * @return certId
     */
    public BigInteger getCertId() {
        return certId;
    }

    /**
     * Set certId
     * @param certId
     */
    public void setCertId(BigInteger certId) {
        this.certId = certId;
    }

    /**
     * Get csId
     * @return csId
     */
    public String getCsId() {
        return csId;
    }

    /**
     * Set csId
     * @param csId
     */
    public void setCsId(String csId) {
        this.csId = csId;
    }

    /**
     * Get CommonName
     * @return CommonName
     */
    public String getCommonName() {
        return CommonName;
    }

    /**
     * Set CommonName
     * @param commonName
     */
    public void setCommonName(String commonName) {
        CommonName = commonName;
    }

    /**
     * Get fileLocation
     * @return fileLocation
     */
    public String getFileLocation() {
        return fileLocation;
    }

    /**
     * Set fileLocation
     * @param fileLocation
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Get certStatus
     * @return certStatus
     */
    public String getCertStatus() {
        return certStatus;
    }

    /**
     * Set certStatus
     * @param certStatus
     */
    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    /**
     * Get expiredDate
     * @return expiredDate
     */
    public Date getExpiredDate() {
        return expiredDate;
    }

    /**
     * Set expiredDate
     * @param expiredDate
     */
    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    /**
     * Get writer
     * @return writer
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * Set writer
     * @param writer
     */
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Get caType
     * @return caType
     */
    public String getCaType() {
        return caType;
    }

    /**
     * Set caType
     * @param caType
     */
    public void setCaType(String caType) {
        this.caType = caType;
    }
    
}
