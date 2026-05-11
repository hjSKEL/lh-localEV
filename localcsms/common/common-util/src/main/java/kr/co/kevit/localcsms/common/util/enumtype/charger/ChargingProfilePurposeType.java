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
public enum ChargingProfilePurposeType implements EnumInterface {
    ChargePointMaxProfile("EBPP01", ""),
    TxDefaultProfile("EBPP02", ""),
    TxProfile("EBPP03", "");
    
    private String code;

    private String desc;

    ChargingProfilePurposeType(String code, String desc) {
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

    public static ChargingProfilePurposeType getTypeByCode(String code) {
        ChargingProfilePurposeType[] values = values();
        for (ChargingProfilePurposeType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
