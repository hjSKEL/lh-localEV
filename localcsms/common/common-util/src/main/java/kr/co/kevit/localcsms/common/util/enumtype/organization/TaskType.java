/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.organization;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public enum TaskType implements EnumInterface {

    FIELD("FI", "현장"),
    SAFETY("SA", "안전"),
    MAKER("MK", "제조사")
    ;

    private String code;
    private String desc;

    TaskType(String code, String desc) {
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

    public static TaskType getFeeType(String code) {
        for (TaskType type : TaskType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static TaskType getTypeByCode(String code) {
        TaskType[] values = values();
        for (TaskType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        TaskType[] values = TaskType.values();
        for (TaskType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}