/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
<DERControlList href="/sep2/A1/derp/1/derc" subscribable="1" all="1" results="1" xmlns="urn:ieee:std:2030.5:ns">
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
</DERControlList>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlRootElement(name = "DERControlList", namespace = "urn:ieee:std:2030.5:ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class DERControlList {
    
    /**
     * 
    ="/sep2/A1/derp/1/derc" 
     */
    @XmlAttribute(name = "href")
    private String href;
    
    /**
     * 
    ="1" 
     */
    @XmlAttribute(name = "subscribable")
    private String subscribable;
    /**
     * 
    ="1"
     */
    @XmlAttribute(name = "all")
    private String all;
    /**
     * 
    ="1"
     */
    @XmlAttribute(name = "results")
    private String results;
    /**
     * 
    ="urn:ieee:std:2030.5:ns">
     */
//    @XmlAttribute(name = "xmlns")
//    private String xmlns;
    
    @XmlElement(name = "DERControl", namespace = "urn:ieee:std:2030.5:ns")
    private List<DERControl> derControls;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSubscribable() {
        return subscribable;
    }

    public void setSubscribable(String subscribable) {
        this.subscribable = subscribable;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

//    public String getXmlns() {
//        return xmlns;
//    }
//
//    public void setXmlns(String xmlns) {
//        this.xmlns = xmlns;
//    }

    public List<DERControl> getDerControls() {
        return derControls;
    }

    public void setDerControls(List<DERControl> derControls) {
        this.derControls = derControls;
    }

}
