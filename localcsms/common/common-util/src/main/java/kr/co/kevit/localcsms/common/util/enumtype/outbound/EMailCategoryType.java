/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.outbound;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 2. 7.
 */
public enum EMailCategoryType implements EnumInterface {
	//
    CUSTOMER("MSGA01", "회원"),
    RESERVATION("MSGA02", "예약"),
    GIFT("MSGA03", "선물"),
    COOPON("MSGA04", "쿠폰"),
    ELEC_BILL("MSGA05", "완속 충전 전기요금 청구서"),
    MAINTENANCE("MSGA06", "정비")
    ;
	
	EMailCategoryType(String code, String desc) {
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

    public static EMailCategoryType getTypeByCode(String code) {
    	EMailCategoryType[] values = values();
        for (EMailCategoryType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        EMailCategoryType[] values = EMailCategoryType.values();
        for (EMailCategoryType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
