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

/*
        <DERCurve href="/sep2/dc/1">
                <mRID>C0000001</mRID>
                <description>Volt-Watt Curve 1</description>
                <creationTime>1514836800</creationTime>
                <CurveData>
                        <xvalue>0</xvalue>
                        <yvalue>100</yvalue>
                </CurveData>
                <CurveData>
                        <xvalue>220</xvalue>
                        <yvalue>100</yvalue>
                </CurveData>
                <CurveData>
                        <xvalue>230</xvalue>
                        <yvalue>100</yvalue>
                </CurveData>
                <CurveData>
                        <xvalue>240</xvalue>
                        <yvalue>50</yvalue>
                </CurveData>
                <CurveData>
                        <xvalue>250</xvalue>
                        <yvalue>0</yvalue>
                </CurveData>
                <curveType>3</curveType>
                <xMultiplier>0</xMultiplier>
                <yMultiplier>0</yMultiplier>
                <yRefType>0</yRefType>
        </DERCurve>
 */
/**
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DERCurve {
    
    @XmlAttribute(name = "href")
    private String href;
    
    @XmlElement(name = "mRID",namespace = "urn:ieee:std:2030.5:ns")
    private String mRID;
    
    @XmlElement(name = "description",namespace = "urn:ieee:std:2030.5:ns")
    private String description;
    
    @XmlElement(namespace = "urn:ieee:std:2030.5:ns")
    private long creationTime;

    @XmlElement(name = "CurveData", namespace = "urn:ieee:std:2030.5:ns")
    private List<CurveData> curveData;

    @XmlElement(name = "curveType",namespace = "urn:ieee:std:2030.5:ns")
    private int curveType;

    @XmlElement(name = "xMultiplier",namespace = "urn:ieee:std:2030.5:ns")
    private int xMultiplier;

    @XmlElement(name = "yMultiplier",namespace = "urn:ieee:std:2030.5:ns")
    private int yMultiplier;

    @XmlElement(name = "yRefType",namespace = "urn:ieee:std:2030.5:ns")
    private int yRefType;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public List<CurveData> getCurveData() {
        return curveData;
    }

    public void setCurveData(List<CurveData> curveData) {
        this.curveData = curveData;
    }

    public int getCurveType() {
        return curveType;
    }

    public void setCurveType(int curveType) {
        this.curveType = curveType;
    }

    public int getxMultiplier() {
        return xMultiplier;
    }

    public void setxMultiplier(int xMultiplier) {
        this.xMultiplier = xMultiplier;
    }

    public int getyMultiplier() {
        return yMultiplier;
    }

    public void setyMultiplier(int yMultiplier) {
        this.yMultiplier = yMultiplier;
    }

    public int getyRefType() {
        return yRefType;
    }

    public void setyRefType(int yRefType) {
        this.yRefType = yRefType;
    }
}
