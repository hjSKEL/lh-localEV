/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.authority;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 30.
 */
public enum DeviceType implements EnumInterface {

    ANDROID("DVTAND", "Android"),
    IOS("DVTIOS", "ios");

    private String code;

    private String desc;

    private DeviceType(String code, String desc) {
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

    public static DeviceType getTypeByCode(String code) {
        DeviceType[] values = values();
        for (DeviceType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}