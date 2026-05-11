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
 * @since 2018. 11. 20.
 */
public enum PgBGType implements EnumInterface {

    NA("A", "N/A"),
    PERSONAL_CARD("B", "개인신용카드"),
    BIZ_CARD("C", "법인신용카드")
    ;

    private String code;
    private String desc;

    PgBGType(String code, String desc) {
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

    public static PgBGType getPgBGType(String code) {
        for (PgBGType type : PgBGType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static PgBGType getTypeByCode(String code) {
        PgBGType[] values = values();
        for (PgBGType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        PgBGType[] values = PgBGType.values();
        for (PgBGType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}