/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.charger;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 수리 진행 상태
 * ACCC00
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 1. 4.
 */
public enum RepairStatus implements EnumInterface {
    RECEIPT("ACCC09", "접수"), 
    ING("ACCC02", "진행"), 
    FINISH("ACCC01", "종료");

    private String code;
    
    private String desc;

    RepairStatus(String code, String desc) {
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

    public static RepairStatus getRepairStatus(String code) {
        for (RepairStatus type : RepairStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
    
    public static RepairStatus getTypeByCode(String code) {
    	RepairStatus[] values = values();
        for (RepairStatus value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        RepairStatus[] values = RepairStatus.values();
        for (RepairStatus value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
