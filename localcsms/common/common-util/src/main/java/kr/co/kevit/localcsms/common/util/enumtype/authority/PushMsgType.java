/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.authority;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 30.
 */
public enum PushMsgType implements EnumInterface {

    RECEIPT("BDPU01", "고장접수"),
    REPAIR("BDPU02", "고장수리"),
    CHAT("BDPU03", "채팅");

    private String code;

    private String desc;

    private PushMsgType(String code, String desc) {
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

    public static PushMsgType getTypeByCode(String code) {
        PushMsgType[] values = values();
        for (PushMsgType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
}