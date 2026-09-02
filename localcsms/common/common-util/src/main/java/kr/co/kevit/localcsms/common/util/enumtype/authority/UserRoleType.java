/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.authority;

import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyCodeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 역할
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 13.
 */
public enum UserRoleType implements EnumInterface {

    //GUEST("ROLE_GUEST", "비회원고객"),
    //USER("ROLE_USER", "충전기 사용자"),
    OPERATION("ROLE_OPER", "운영관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    ROOT_ADMIN("ROLE_ROOT_ADMIN", "최고관리자"),
    //CONSULT_STAFF("ROLE_CS", "상담사(S)"),
    //CONSULT_MASTER("ROLE_CM", "상담관리자(M)")
    ;

    private String code;

    private String desc;

    private UserRoleType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserRoleType getRoleType(String roleString) {
        //
        if (StringUtils.isEmpty(roleString)) {
            return null;
        }

        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.toString().equals(roleString)) {
                return userRoleType;
            }
        }
        return null;
    }

    public static UserRoleType getRoleTypeByCode(String roleCodeString) {
        //
        if (StringUtils.isEmpty(roleCodeString)) {
            return null;
        }

        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getCode().equals(roleCodeString)) {
                return userRoleType;
            }
        }
        return null;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static UserRoleType getTypeByCode(String code) {
        UserRoleType[] values = values();
        for (UserRoleType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyCodeValue> getKeyCodeValues() {
        List<EnumKeyCodeValue> enumKeyCodeValues = new ArrayList<>();

        UserRoleType[] values = UserRoleType.values();
        for (UserRoleType value : values) {
            enumKeyCodeValues.add(new EnumKeyCodeValue(value.name(), value.getCode(), value.getDesc()));
        }
        return enumKeyCodeValues;
    }

    public static EnumKeyCodeValue getKeyCodeValue(UserRoleType userRoleType) {
        //
        return new EnumKeyCodeValue(userRoleType.name(), userRoleType.getCode(), userRoleType.getDesc());
    }
}
