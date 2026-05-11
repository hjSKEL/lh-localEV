/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/*
                <EventStatus>
                        <currentStatus>0</currentStatus>
                        <dateTime>1514838000</dateTime>
                        <potentiallySuperseded>false</potentiallySuperseded>
                </EventStatus>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EventStatus {
    
    @XmlElement(name = "currentStatus",namespace = "urn:ieee:std:2030.5:ns")
    private Integer currentStatus;
    
    @XmlElement(name = "dateTime",namespace = "urn:ieee:std:2030.5:ns")
    private Long dateTime;
    
    @XmlElement(name = "potentiallySuperseded",namespace = "urn:ieee:std:2030.5:ns")
    private Boolean potentiallySuperseded;

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getPotentiallySuperseded() {
        return potentiallySuperseded;
    }

    public void setPotentiallySuperseded(Boolean potentiallySuperseded) {
        this.potentiallySuperseded = potentiallySuperseded;
    }
}
