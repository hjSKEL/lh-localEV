/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.charger;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 12. 11.
 */
public enum BreakdownStatus implements EnumInterface {

    BDST01("BDST01", "접수"),
    BDST02("BDST02", "현장점검확인"),
    BDST03("BDST03", "현장점검중"),
    BDST04("BDST04", "제조사이첩"),
    BDST05("BDST05", "제조사점검확인"),
    BDST06("BDST06", "조치중"),
    BDST07("BDST07", "조치완료"),
    BDST08("BDST08", "조치불가");

    private String code;

    private String desc;

    BreakdownStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static BreakdownStatus getTypeByCode(String code) {
        BreakdownStatus[] values = values();
        for (BreakdownStatus value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}