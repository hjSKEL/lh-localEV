/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.certificate.entity.domain;

import java.io.Serializable;

import kr.co.kevit.localcsms.common.domain.Writer;

/**
 * TB_CACU001
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 18.
 */
public class CustomerCert implements Serializable{

    /**  */
    private static final long serialVersionUID = -471743248698257442L;

    /**
     * eMaid
     * EMAID VARCHAR(14)
     */
    private String eMaid;
    
    /**
     * EV PCID
     * PCID VARCHAR(20)
     */
    private String pcid;
    
    /**
     * 사용자아이디
     * CUT_ID CHAR(9 BYTE)   NOT NULL,
     */
    private String customerId;
    
    /**
     * 인증서 시러얼 번호.
     * SN VARCHAR(100)  NOT NULL,
     */
    private String serialNumber;
    
    /**
     * SUBJECT DN
     * SUB_DN VARCHAR(100)  NOT NULL,
     */
    private String subjectDn;
    
    /**
     * 
     * XSD_MSG_DEF_NMSP VARCHAR(20)
     */
    private String xsdMsgDefNamespace;
    
    /**
     * 유효 시작 일
     * CERT_VAL_FROM CHAR(8)
     */
    private String certValidFrom;
    
    /**
     * 유효 종료 일
     * CERT_VAL_TO CHAR(8)
     */
    private String certValidTo;
    
    /**
     * STAT CHAR(6)
     * CERT01 생성/갱신요청
     * CERT02 생성/갱신완료
     * CERT03 EV설치
     * CERT04 만료
     * CERT05 폐기 
     **/
    private String status;
    
    /**
     * OCSP_RESP_URL VARCHAR(50)
     */
    private String ocspResponderURL;
    
    private Writer writer;

    public String geteMaid() {
        return eMaid;
    }

    public void seteMaid(String eMaid) {
        this.eMaid = eMaid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getXsdMsgDefNamespace() {
        return xsdMsgDefNamespace;
    }

    public void setXsdMsgDefNamespace(String xsdMsgDefNamespace) {
        this.xsdMsgDefNamespace = xsdMsgDefNamespace;
    }

    public String getCertValidFrom() {
        return certValidFrom;
    }

    public void setCertValidFrom(String certValidFrom) {
        this.certValidFrom = certValidFrom;
    }

    public String getCertValidTo() {
        return certValidTo;
    }

    public void setCertValidTo(String certValidTo) {
        this.certValidTo = certValidTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOcspResponderURL() {
        return ocspResponderURL;
    }

    public void setOcspResponderURL(String ocspResponderURL) {
        this.ocspResponderURL = ocspResponderURL;
    }

    public Writer getWriter() {
        return writer;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public String getPcid() {
        return pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }

    public String getSubjectDn() {
        return subjectDn;
    }

    public void setSubjectDn(String subjectDn) {
        this.subjectDn = subjectDn;
    }

}
