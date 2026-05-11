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
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 1. 4.
 */
public enum RepairType implements EnumInterface {

    BROKEN("ACCD01", "충전기고장"),
    POWER_OFF("ACCD02", "충전기 전원OFF"),
    WIRELESS("ACCD03", "통신 장애"),
    CARD_TAG("ACCD04", "카드 태깅 불량"),
    DAMAGED("ACCD05", "충전기 파손"),
    CHARGE_FAULTY("ACCD06", "충전 불량"),
    RECHARGE_MISCALC("ACCD07", "충전량 오계측"),
    BREAKER_FAULTY("ACCD08", "차단기 불량"),
    CABLE("ACCD09", "충전케이블 파손"),
    UNINSTALL("ACCD10", "충전기 미설치"),
    ETC("ACCD99", "기타");

    private String code;

    private String desc;

    RepairType(String code, String desc) {
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

    public static RepairType getTypeByCode(String code) {
        RepairType[] values = values();
        for (RepairType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static RepairType getRepairType(String code) {
        for (RepairType type : RepairType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        RepairType[] values = RepairType.values();
        for (RepairType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
