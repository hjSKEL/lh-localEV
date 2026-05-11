package kr.co.kevit.localcsms.common.util.enumtype.customer;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 은행 사
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 * @since 2018. 5. 24.
 */
public enum CardCategory implements EnumInterface {

    INDIVIDUAL("CRDA01", "개인카드"),
    FAMILY("CRDA02", "가족카드"),
    CORPORATE("CRDA03", "법인카드"),
    CORP_INDIVIDUAL("CRDA04", "법인개인"),
    SPECIAL("CRDA05", "별도정산");

    CardCategory(String code, String desc) {
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

    public static CardCategory getTypeByCode(String code) {
        CardCategory[] values = values();
        for (CardCategory value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        CardCategory[] values = CardCategory.values();
        for (CardCategory value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}