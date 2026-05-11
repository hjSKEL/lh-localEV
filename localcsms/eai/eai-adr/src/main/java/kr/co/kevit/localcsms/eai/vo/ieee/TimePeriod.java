/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
        <timePeriod>
            <duration>0</duration>
            <start>12987364</start>
        </timePeriod>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlRootElement(name = "timePeriod", namespace = "urn:ieee:std:2030.5:ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimePeriod {
    
    @XmlElement(name = "duration",namespace = "urn:ieee:std:2030.5:ns")
    private int duration;
    
    @XmlElement(name = "start",namespace = "urn:ieee:std:2030.5:ns")
    private long start;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

}
