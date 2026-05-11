/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.roaming;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 12. 17.
 */
public enum ApproxNumberType implements EnumInterface {
    //
    ROUND_FLOOR  ("RMRD01", "ROUND_FLOOR"),
    ROUND_CEILING("RMRD02", "ROUND_CEILING"),
    ROUND_HALF_UP("RMRD03", "ROUND_HALF_UP");

    private String code;
    private String desc;

    ApproxNumberType(String code, String desc) {
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

    public static ApproxNumberType getTypeByCode(String code) {
        ApproxNumberType[] values = values();
        for (ApproxNumberType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ApproxNumberType[] values = ApproxNumberType.values();
        for (ApproxNumberType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}