/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/*
                <CurveData>
                        <xvalue>0</xvalue>
                        <yvalue>100</yvalue>
                </CurveData>
 */
/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CurveData {
    
    @XmlElement(name="xvalue", namespace = "urn:ieee:std:2030.5:ns")
    private int xvalue;

    @XmlElement(name="yvalue", namespace = "urn:ieee:std:2030.5:ns")
    private int yvalue;

    public int getXvalue() {
        return xvalue;
    }

    public void setXvalue(int xvalue) {
        this.xvalue = xvalue;
    }

    public int getYvalue() {
        return yvalue;
    }

    public void setYvalue(int yvalue) {
        this.yvalue = yvalue;
    }
    
}
