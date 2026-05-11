/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/*
        <DERControl href="/sep2/A1/derp/1/derc/1" replyTo="/rsps/1/rsp" responseRequired="03">
                <mRID>D0000001</mRID>
                <description>Scheduled DERC</description>
                <creationTime>1514838000</creationTime>
                <EventStatus>
                        <currentStatus>0</currentStatus>
                        <dateTime>1514838000</dateTime>
                        <potentiallySuperseded>false</potentiallySuperseded>
                </EventStatus>
                <interval>
                        <duration>3600</duration>
                        <start>1514926800</start>
                </interval>
                <DERControlBase>
                        <opModVoltWatt href="some_uri" />
                </DERControlBase>
        </DERControl>
 */
/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DERControl {
    
    @XmlAttribute(name = "href")
    private String href;
    
    @XmlAttribute(name = "replyTo")
    private String replyTo;
    
    @XmlAttribute(name = "responseRequired")
    private String responseRequired;
    
    @XmlElement(name = "mRID",namespace = "urn:ieee:std:2030.5:ns")
    private String mRID;
    
    @XmlElement(name = "description",namespace = "urn:ieee:std:2030.5:ns")
    private String description;
    
    @XmlElement(name = "creationTime",namespace = "urn:ieee:std:2030.5:ns")
    private Long creationTime;
    
    @XmlElement(name = "EventStatus",namespace = "urn:ieee:std:2030.5:ns")
    private EventStatus eventStatus;
    
    @XmlElement(name = "interval",namespace = "urn:ieee:std:2030.5:ns")
    private Interval interval;
    
    @XmlElement(name = "DERControlBase",namespace = "urn:ieee:std:2030.5:ns")
    private DERControlBase derControlBase;
    
    
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getResponseRequired() {
        return responseRequired;
    }

    public void setResponseRequired(String responseRequired) {
        this.responseRequired = responseRequired;
    }

    public String getmRID() {
        return mRID;
    }

    public void setmRID(String mRID) {
        this.mRID = mRID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public DERControlBase getDerControlBase() {
        return derControlBase;
    }

    public void setDerControlBase(DERControlBase derControlBase) {
        this.derControlBase = derControlBase;
    }
    
}
