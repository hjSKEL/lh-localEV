/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.enumtype;

/**
 * 
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 5. 9.
 */
public class EnumKeyCodeValue {

    private String key;

    private String code;

    private String value;

    public EnumKeyCodeValue(String key, String code, String value) {
        super();
        this.key = key;
        this.value = value;
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
