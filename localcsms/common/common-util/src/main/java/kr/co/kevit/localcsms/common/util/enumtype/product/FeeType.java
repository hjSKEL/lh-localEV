/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.product;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 2.
 */
public enum FeeType  implements EnumInterface {

    CSON_UNIT_PRICE("FEE001", "계절별 단가"),
    FIX_UNIT_PRICE("FEE002", "단일단가"),
    LOW_FIX_UNIT_PRICE_FIX_CUST("FEE003", "저속단일단가/특정회원만0원"),
    HI_FIX_UNIT_PRICE_FIX_CUST("FEE004", "고압단일단가/회원/특별0원")
    ;

    private String code;
    private String desc;

    FeeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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

    public static FeeType getFeeType(String code) {
        for (FeeType type : FeeType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static FeeType getTypeByCode(String code) {
        FeeType[] values = values();
        for (FeeType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        FeeType[] values = FeeType.values();
        for (FeeType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}