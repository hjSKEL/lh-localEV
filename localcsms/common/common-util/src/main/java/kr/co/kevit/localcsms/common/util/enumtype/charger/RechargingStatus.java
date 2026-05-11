/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.charger;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 17.
 */
public enum RechargingStatus implements EnumInterface {

    RECS01("RECS01", "시작"),
    RECS02("RECS02", "충전중"),
    RECS03("RECS03", "종료"),
    RECS04("RECS04", "취소"),
    RECS05("RECS05", "결제");

    private String code;
    private String desc;

    RechargingStatus(String code, String desc) {
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

    public static RechargingStatus getTypeByCode(String code) {
        RechargingStatus[] values = values();
        for (RechargingStatus value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static ReservationStatus getReservationStatus(String code) {
        for (ReservationStatus type : ReservationStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        RechargingStatus[] values = RechargingStatus.values();
        for (RechargingStatus value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }
        return nameValues;
    }
}