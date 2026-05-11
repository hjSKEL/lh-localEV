/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.customer;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 5. 20.
 */
public enum ReCustomerCardStatus  implements EnumInterface {
    ///완료 
    Request("CRDD01", "요청"),
    Complate("CRDD02", "완료");

    ReCustomerCardStatus(String code, String desc) {
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
    
    public static ReCustomerCardStatus getReCustomerCardStatus(String code){
        for(ReCustomerCardStatus status : ReCustomerCardStatus.values()){
            if(status.getCode().equals(code)){
                return status;
            }
        }
        return null;
    }
    
    public static ReCustomerCardStatus getTypeByCode(String code) {
        ReCustomerCardStatus[] values = values();
        for (ReCustomerCardStatus value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ReCustomerCardStatus[] values = ReCustomerCardStatus.values();
        for (ReCustomerCardStatus value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}