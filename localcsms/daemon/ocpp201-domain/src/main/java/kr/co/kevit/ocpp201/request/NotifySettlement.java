/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.domain.AddressType;
import kr.co.kevit.ocpp201.enumtype.PaymentStatusEnumType;

/**
 * (2.1)
 */
public class NotifySettlement {

    /**
     * required
     */
    private String pspRef;

    /**
     * required
     */
    private PaymentStatusEnumType status;

    /**
     * required
     */
    private double settlementAmount;

    /**
     * required
     */
    private String settlementTime;

    private String transactionId;

    private String statusInfo;

    private String receiptId;

    private String receiptUrl;

    private AddressType vatCompany;

    private String vatNumber;

    private Map<String, Object> customData;

    public String getPspRef() {
        return pspRef;
    }

    public void setPspRef(String pspRef) {
        this.pspRef = pspRef;
    }

    public PaymentStatusEnumType getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusEnumType status) {
        this.status = status;
    }

    public double getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(double settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(String settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public AddressType getVatCompany() {
        return vatCompany;
    }

    public void setVatCompany(AddressType vatCompany) {
        this.vatCompany = vatCompany;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
