/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.charger;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 1.
 */
public enum ReservationStatus implements EnumInterface {

    RESA01("RESA01", "예약"),
    RESA02("RESA02", "이용중"),
    RESA03("RESA03", "종료"),
    RESA04("RESA04", "취소"),
    RESA05("RESA05", "이용안함"),
    RESA06("RESA06", "리턴중"),
    RESA07("RESA07", "변경");
    

    private String code;
    
    private String desc;

    ReservationStatus(String code, String desc) {
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
    
    public static ReservationStatus getTypeByCode(String code) {
    	ReservationStatus[] values = values();
        for (ReservationStatus value : values) {
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

        ReservationStatus[] values = ReservationStatus.values();
        for (ReservationStatus value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}