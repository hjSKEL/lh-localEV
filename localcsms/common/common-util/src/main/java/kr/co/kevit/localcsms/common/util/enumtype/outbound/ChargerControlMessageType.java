/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.outbound;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 7.
 */
public enum ChargerControlMessageType implements EnumInterface {
    //
    AUTH("CCM001", "인증"),
    REBOOT("CCM002", "충전기부팅"),
    RESET("CCM003", "초기화"),
    RECONNECT("CCM004", "재연결"),
    RESERVATION("CCM005", "예약"),
    STATUS("CCM006", "상태"),
    SOUND("CCM007", "볼륨")
    ;
    ChargerControlMessageType(String code, String desc) {
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

    public static ChargerControlMessageType getTypeByCode(String code) {
        ChargerControlMessageType[] values = values();
        for (ChargerControlMessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ChargerControlMessageType[] values = ChargerControlMessageType.values();
        for (ChargerControlMessageType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}