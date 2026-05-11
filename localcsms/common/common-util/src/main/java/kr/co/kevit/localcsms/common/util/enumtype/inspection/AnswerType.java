/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.inspection;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 10.
 */
public enum AnswerType implements EnumInterface {
    
    SELECT("S", "O,X,-"),
    INPUT("I", "INPUT"),
    IMAGE("M", "IMG"),
    TEXTAREA("T","TEXTAREA");
    AnswerType(String code, String desc) {
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

    public static AnswerType getTypeByCode(String code) {
        AnswerType[] values = values();
        for (AnswerType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        AnswerType[] values = AnswerType.values();
        for (AnswerType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}