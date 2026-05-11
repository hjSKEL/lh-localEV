/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype.authority;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 메뉴 카테고리
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 12. 13.
 */
public enum MenuCategory implements EnumInterface{

    LOGIN("LI", "로그인"),
    JOIN("JI", "가입"),
    USER_MANAGEMENT("UM", "사용자관리"),
    NOTICE("NT", "공지사항"),
    ADJUSTMENT("AD", "정산"),
    CONSULTATION("CO", "상담"),
    FIELD_INSPECTOR("FI","현장점검"),
    CONTROL("CR", "관제"),
    PRODUCT("PD", "상품"),
    CUSTOEMR_SERVICE("CS", "CS관리"),
    SYSTEM_MANAGEMENT("SM", "시스템관리"),

    /** 업무 로직에서 사용하지 않도록 한다. */
    INIT("INIT", "초기 최상위 메뉴");

    private String code;
    private String desc;

    private MenuCategory(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /** 메뉴 아이디 채번 길이 */
    private static final int DEFAULT_MENU_ID_SIZE = 6;

    /**
     * 아이디 채번
     * 
     * @param sequence
     */
    public String retrieveGenerateId(int sequence) {
        // sequence 길이(ex : 0 = 1, 10 => 2, 143 = > 3)
        int sequenceLength = String.valueOf(sequence).length();
        int rpadSize = DEFAULT_MENU_ID_SIZE - sequenceLength;
        return StringUtils.rightPad(this.code, rpadSize, "0") + sequence;
    }

    public static MenuCategory getTypeByCode(String code) {
        MenuCategory[] values = values();
        for (MenuCategory menuCategory : values) {
            if (menuCategory.getCode().equals(code)) {
                return menuCategory;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        MenuCategory[] values = MenuCategory.values();
        for (MenuCategory menuCategory : values) {
            if (!MenuCategory.INIT.equals(menuCategory)) {
                nameValues.add(new EnumKeyValue(menuCategory.name(), menuCategory.getDesc()));
            }
        }

        return nameValues;
    }

    @Override
    public String getCode() {
        // 
        return this.code;
    }

    @Override
    public String getDesc() {
        //
        return this.desc;
    }

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
