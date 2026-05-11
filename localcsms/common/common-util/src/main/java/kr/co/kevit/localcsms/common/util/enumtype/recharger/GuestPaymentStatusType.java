/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.recharger;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 9.
 */
public enum GuestPaymentStatusType implements EnumInterface {

    PAYE01("PAYE01", "선결제"),
    PAYE02("PAYE02", "재결제요청"),
    PAYE03("PAYE03", "재결제실패"),    
    PAYE04("PAYE04", "결제완료")
    ;

    private String code;
    private String desc;

    GuestPaymentStatusType(String code, String desc) {
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

    public static GuestPaymentStatusType getSimplePaymentStatusType(String code) {
        for (GuestPaymentStatusType type : GuestPaymentStatusType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static GuestPaymentStatusType getTypeByCode(String code) {
        GuestPaymentStatusType[] values = values();
        for (GuestPaymentStatusType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        GuestPaymentStatusType[] values = GuestPaymentStatusType.values();
        for (GuestPaymentStatusType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}