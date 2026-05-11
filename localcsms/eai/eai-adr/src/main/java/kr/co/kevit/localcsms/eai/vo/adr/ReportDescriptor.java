/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 11.
 */
public class ReportDescriptor {
    
    /**
     * 
    : Enumerated or private string signifying the nature of values, e.g. "USAGE".
     */
    private String payloadType;
    
    /**
    : Enumerated or private string signifying the type of reading, e.g. "DIRECT_READ".
     * 
     */
    private String readingType;
    
    /**
    : units of measure, e.g. "KWH".
     */
    private String units;
    
    /**
     * 
    : An array of valuesMap objects.
     */
    private List<ValuesMap>targets;
    
    /**
     * 
    : True if report should aggregate results from all targeted resources [false]
     */
    private Boolean aggregate;
    
    /**
    : The interval on which to generate a report. [-1]
     */
    private String startInterval;
    
    /**
     * 
    : The number of intervals to include in a report. [-1]
     */
    private Integer numIntervals;
    
    /**
     * 
    : True indicates report on intervals preceding startInterval. [true]
     */
    private Boolean historical;
    
    /**
    : Number of intervals that elapse between reports. [-1]
     * 
     */
    private Integer frequency;
    
    /**
    : Number of times to repeat a report. [1]
     */
    private Integer repeat;

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<ValuesMap> getTargets() {
        return targets;
    }

    public void setTargets(List<ValuesMap> targets) {
        this.targets = targets;
    }

    public Boolean getAggregate() {
        return aggregate;
    }

    public void setAggregate(Boolean aggregate) {
        this.aggregate = aggregate;
    }

    public String getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(String startInterval) {
        this.startInterval = startInterval;
    }

    public Integer getNumIntervals() {
        return numIntervals;
    }

    public void setNumIntervals(Integer numIntervals) {
        this.numIntervals = numIntervals;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

}
