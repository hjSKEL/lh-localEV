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
public class ReportPayloadDescriptor {
    /**
     * payloadType: Enumerated or private string signifying the nature of values, e.g. "USAGE".
    ": "PRICE""
     */
    private String payloadType;
    
    /**
     * units: units of measure, e.g. "KWH".
    ": "KWH""
     */
    private String units;
    /**
     * 
    : Enumerated or private string signifying the type of reading,
    e.g. "DIRECT_READ". ["DIRECT_READ"]
     */
    private String readingType;
    
    /**
     * 
    : a quantification of the accuracy of a set of payload values.
     */
    private String accuracy;
    
    /**
     * 
    : a quantification of the confidence in a set of payload values.
     */
    private String confidence;

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

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

}
