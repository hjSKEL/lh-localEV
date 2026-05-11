/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 11.
 */
public class Event {
    
    /**
    : VTN provisioned ID of this object instance.
     */
    private String id;
    
    /**
    : server provisions timestamp on object creation, e.g.
    "2023-06-15T12:58:08.000Z".
     */
    private String createdDateTime;
    
    /**
    : server provisions timestamp on object modification,
    e.g. "2023-06-16T12:58:08.000Z".
     */
    private String modificationDateTime;
    
    /**
    : Used as discriminator. EVENT
     */
    private String objectType;
    
    /**
    : ID attribute of program object this event is associated with.
     */
    private String programID;
    
    /**
    : User defined string for use in debugging or UI, e.g. "price event 11-18-2022".
    priority: relative priority of event. A lower number is a higher priority.
     */
    private String eventName;
    
    /**
     * 
    : An array of valuesMap objects.
     */
    private List<ValuesMap> targets;
    
    /**
     * 
    : An array of reportDescriptor objects. Used to request reports from VEN.
     */
    private List<ReportDescriptor> reportDescriptors;
    
    /**
    : An array of payloadDescriptor objects.
     * 
     */
    private List<PayloadDescriptor> payloadDescriptors;
    
    /**
     * 
    : Defines default start and durations of intervals.
     */
    private IntervalPeriod intervalPeriod;
    
    /**
    : An array of interval objects
     */
    private List<Interval> intervals;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(String modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<ValuesMap> getTargets() {
        return targets;
    }

    public void setTargets(List<ValuesMap> targets) {
        this.targets = targets;
    }

    public List<ReportDescriptor> getReportDescriptors() {
        return reportDescriptors;
    }

    public void setReportDescriptors(List<ReportDescriptor> reportDescriptors) {
        this.reportDescriptors = reportDescriptors;
    }

    public List<PayloadDescriptor> getPayloadDescriptors() {
        return payloadDescriptors;
    }

    public void setPayloadDescriptors(List<PayloadDescriptor> payloadDescriptors) {
        this.payloadDescriptors = payloadDescriptors;
    }

    public IntervalPeriod getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(IntervalPeriod intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public List<Interval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

}
