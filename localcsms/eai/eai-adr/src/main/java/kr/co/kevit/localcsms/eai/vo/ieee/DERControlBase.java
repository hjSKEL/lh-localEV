/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.ieee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/*
                <DERControlBase>
                        <opModVoltWatt href="some_uri" />
                </DERControlBase>
 */
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DERControlBase {
    
    @XmlElement(name = "opModVoltWatt",namespace = "urn:ieee:std:2030.5:ns")
    private OpModVoltWatt opModVoltWatt;

    public OpModVoltWatt getOpModVoltWatt() {
        return opModVoltWatt;
    }

    public void setOpModVoltWatt(OpModVoltWatt opModVoltWatt) {
        this.opModVoltWatt = opModVoltWatt;
    }
    
}
