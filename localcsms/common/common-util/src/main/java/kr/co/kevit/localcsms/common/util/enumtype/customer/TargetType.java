package kr.co.kevit.localcsms.common.util.enumtype.customer;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 은행 사
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 * @since 2018. 5. 24.
 */
public enum TargetType implements EnumInterface {

    User("USER", "개인"),
    Company("COMPANY", "법인");

    TargetType(String code, String desc) {
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

    public static TargetType getTypeByCode(String code) {
        TargetType[] values = values();
        for (TargetType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        TargetType[] values = TargetType.values();
        for (TargetType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}