/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 11.
 */
public class PayloadDescriptor {
    
    /**
     * payloadType: Enumerated or private string signifying the nature of values, e.g. "USAGE".
    ": "PRICE, EXPORT_CAPACITY_LIMIT
     */
    private String payloadType;
    
    /**
     * units: units of measure, e.g. "KWH".
    ": "KWH, AMPS
     */
    private String units;
    
    /**
     * 
    ": "USD"}
     */
    private String currency;

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
