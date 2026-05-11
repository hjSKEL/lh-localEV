/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/*
                <interval>
                        <duration>3600</duration>
                        <start>1514926800</start>
                </interval>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Interval {
    
    @XmlElement(name = "duration",namespace = "urn:ieee:std:2030.5:ns")
    private Integer duration;
    
    @XmlElement(name = "start",namespace = "urn:ieee:std:2030.5:ns")
    private Long start;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }
    
}
