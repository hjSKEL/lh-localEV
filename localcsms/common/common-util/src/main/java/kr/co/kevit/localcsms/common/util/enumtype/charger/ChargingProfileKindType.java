/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.charger;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 6. 10.
 */
public enum ChargingProfileKindType implements EnumInterface {
    Absolute("EBPK01",""),
    Recurring("EBPK02",""),
    Relative("EBPK03","");
    
    private String code;

    private String desc;

    ChargingProfileKindType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static ChargingProfileKindType getTypeByCode(String code) {
        ChargingProfileKindType[] values = values();
        for (ChargingProfileKindType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
