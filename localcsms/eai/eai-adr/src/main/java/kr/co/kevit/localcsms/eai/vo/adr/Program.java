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
public class Program {
    
    /**
     * 
    Id: VTN provisioned ID of this object instance.
     */
    private String id;
    
    /**
     * 
    : Creation time for object, e.g. "2023-06-15T12:58:08.000Z".
     */
    private String createdDateTime;
    
    /**
     * 
    : Modification time for object, e.g. "2023-06-16T12:58:08.000Z".
     */
    private String modificationDateTime;
    
    /**
     * 
    : Used as discriminator. PROGRAM
     */
    private String objectType;
    
    /**
     * 
    : Name of program with which this event is associated, e.g. "ResTOU".
     */
    private String programName;
    
    /**
     * 
    : User provided ID, e.g. "Residential Time of Use-A".
     */
    private String programLongName;
    
    /**
     * 
    : Program defined ID, e.g. "ACME".
     */
    private String retailerName;
    
    /**
     * 
    : Program defined ID, e.g. "ACME Electric Inc.".
     */
    private String retailerLongName;
    
    /**
     * 
    : User defined string categorizing the program, e.g. "PRICING_TARIFF".
     */
    private String programType;
    
    /**
     * 
    : Alpha-2 code per ISO 3166-1, e.g. "US".
     */
    private String country;
    
    /**
     * 
    : Coding per ISO 3166-2. E.g. state in US, e.g. "CO".
     */
    private String principalSubdivision;
    
    /**
     * 
    : An ISO 8601 duration that is to added to all interval.start values.
     */
    private String timeZoneOffset;
    
    /**
     * 
    : The temporal span of the program, could be years long.
     */
    private IntervalPeriod intervalPeriod;
    
    /**
     * 
    e.g. "mple: www.myCorporation.com/myProgramDescription".
    : List of URLs to human and/or machine-readable content,
     */
    private List<String>programDescriptions;
    
    /**
     * 
    : True if events can be expected to not be modified. [false]
     */
    private Boolean bindingEvents;
    /**
    : True if events have been adapted from a grid event. [false]
     */
    private Boolean localPrice;
    /**
     * 
            : An optional list of objects that provide context to payload types.
     */
    private List<PayloadDescriptor>payloadDescriptors;
    
    /**
     * 
    : An optional list of valuesMap objects.
     */
    private List<ValuesMap>targets;

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

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramLongName() {
        return programLongName;
    }

    public void setProgramLongName(String programLongName) {
        this.programLongName = programLongName;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getRetailerLongName() {
        return retailerLongName;
    }

    public void setRetailerLongName(String retailerLongName) {
        this.retailerLongName = retailerLongName;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrincipalSubdivision() {
        return principalSubdivision;
    }

    public void setPrincipalSubdivision(String principalSubdivision) {
        this.principalSubdivision = principalSubdivision;
    }

    public String getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public IntervalPeriod getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(IntervalPeriod intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public List<String> getProgramDescriptions() {
        return programDescriptions;
    }

    public void setProgramDescriptions(List<String> programDescriptions) {
        this.programDescriptions = programDescriptions;
    }

    public Boolean getBindingEvents() {
        return bindingEvents;
    }

    public void setBindingEvents(Boolean bindingEvents) {
        this.bindingEvents = bindingEvents;
    }

    public Boolean getLocalPrice() {
        return localPrice;
    }

    public void setLocalPrice(Boolean localPrice) {
        this.localPrice = localPrice;
    }

    public List<PayloadDescriptor> getPayloadDescriptors() {
        return payloadDescriptors;
    }

    public void setPayloadDescriptors(List<PayloadDescriptor> payloadDescriptors) {
        this.payloadDescriptors = payloadDescriptors;
    }

    public List<ValuesMap> getTargets() {
        return targets;
    }

    public void setTargets(List<ValuesMap> targets) {
        this.targets = targets;
    }

}
