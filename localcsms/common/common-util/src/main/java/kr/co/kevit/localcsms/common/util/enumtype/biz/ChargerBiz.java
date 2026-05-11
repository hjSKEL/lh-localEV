/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.biz;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 24.
 */
public enum ChargerBiz implements EnumInterface {

    KECO("ME", "환경부"),
    GREENPOWER("GP","그린파워"),
    EVERON("EV", "에버온"),
    KEVIT("KE", "KEVIT");

    private String code;
    
    private String desc;

    ChargerBiz(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    

    @Override
    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public static ChargerBiz getTypeByCode(String code) {
        ChargerBiz[] values = values();
        for (ChargerBiz value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static ChargerBiz getChargerBiz(String code) {
        for (ChargerBiz type : ChargerBiz.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    public static ChargerBiz getChargerBizByCode(String code) {
        ChargerBiz[] values = values();
        for (ChargerBiz value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ChargerBiz[] values = ChargerBiz.values();
        for (ChargerBiz value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
