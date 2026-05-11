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
<DERCurveList href="/sep2/A1/derp/1/dc" all="1" results="1" xmlns="urn:ieee:std:2030.5:ns">
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
</DERCurveList>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlRootElement(name = "DERCurveList", namespace = "urn:ieee:std:2030.5:ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class DERCurveList {
    
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
    @XmlAttribute(name = "all")
    private String all;
    /**
     * 
    ="1"
     */
    @XmlAttribute(name = "results")
    private String results;
    
    @XmlElement(name = "DERCurve", namespace = "urn:ieee:std:2030.5:ns")
    private List<DERCurve> derCurves;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public List<DERCurve> getDerCurves() {
        return derCurves;
    }

    public void setDerCurves(List<DERCurve> derCurves) {
        this.derCurves = derCurves;
    }

}
