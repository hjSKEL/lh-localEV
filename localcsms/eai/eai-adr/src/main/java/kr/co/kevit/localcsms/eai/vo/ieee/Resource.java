/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
    <Resource xsi:type="Reading">
        <timePeriod>
            <duration>0</duration>
            <start>12987364</start>
        </timePeriod>
        <value>1001</value>
    </Resource>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlRootElement(name = "Resource", namespace = "urn:ieee:std:2030.5:ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class Resource {
    
    /**
    * xsi:type="Reading"
    */
   @XmlAttribute(name = "type")
   private String type;
    
    @XmlElement(name = "timePeriod",namespace = "urn:ieee:std:2030.5:ns")
    private TimePeriod timePeriod;
    
    @XmlElement(name = "value",namespace = "urn:ieee:std:2030.5:ns")
    private String value;

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
