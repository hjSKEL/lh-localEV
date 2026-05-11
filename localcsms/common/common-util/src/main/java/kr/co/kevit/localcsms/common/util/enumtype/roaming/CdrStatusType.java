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
 * @author bc.kim <a href="mailto:bc.kim@kevit.co.kr">bc.kim@kevit.co.kr</a> 
 * @since 2020. 10. 27.
 */
public enum CdrStatusType implements EnumInterface {
    //A new CDR before upload to the CHS.
    NEW("RMST01", "NEW"),
    NEW_FAIL("RMST07", "NEW_FAIL"),
    //An uploaded CDR was accepted by the CHS as plausible.
    ACCEPTED("RMST02", "ACCEPTED"),
    //The checked CDR again rejected by the CHS and is to be archived.
    REJECTED("RMST03", "REJECTED"),
    //The CDR was declined by the owner (EVSP).
    DECLINED("RMST04", "DECLINED"),
    //The CDR was approved by the owner (EVSP).
    APPROVED("RMST05", "APPROVED"),
    //The CDR was revised by the CPO and uploaded again. Only previously accepted or declined CDRs can be revised.
    REVISED("RMST06", "REVISED");

    private String code;
    private String desc;

    CdrStatusType(String code, String desc) {
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

    public static CdrStatusType getSimplePaymentStatusType(String code) {
        for (CdrStatusType type : CdrStatusType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static CdrStatusType getTypeByCode(String code) {
        CdrStatusType[] values = values();
        for (CdrStatusType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        CdrStatusType[] values = CdrStatusType.values();
        for (CdrStatusType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}