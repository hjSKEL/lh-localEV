/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.util;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.ocpp16.daemon.util.enumtype.EnumInterface;
import kr.co.kevit.ocpp16.daemon.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 4.
 */
public enum OCPPMsgDirectType implements EnumInterface {

    CP2CSMS("CP2CSMS", "충전기 to 서버"),
    CSMS2CP("CSMS2CP", "서버 to 충전기");

    OCPPMsgDirectType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OCPPMsgDirectType getTypeByCode(String code) {
        OCPPMsgDirectType[] values = values();
        for (OCPPMsgDirectType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        OCPPMsgDirectType[] values = OCPPMsgDirectType.values();
        for (OCPPMsgDirectType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}