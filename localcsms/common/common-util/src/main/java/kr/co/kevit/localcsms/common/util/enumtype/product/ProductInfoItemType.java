package kr.co.kevit.localcsms.common.util.enumtype.product;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 정보항목 타입
 * 
 * @author Bckim
 *
 */
public enum ProductInfoItemType implements EnumInterface {

    BASE("PI000", "기본료"),
    PRE_FEE("PI001", "선결제 금액"),
    MEMBER_UNIT_FEE("PI002", "회원고정단가"),
    GUEST_UNIT_FEE("PI003", "GUEST고정단가"),
    FIX_MEMBER_UNIT_FEE("PI004", "특정회원고정단가"),
    FIX_CP_MEMBER_DISCOUNT("PI005", "특정장소회원 할인율"),
    ;

    private String code;
    private String desc;

    ProductInfoItemType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

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

    public static ProductInfoItemType getProductInfoItemType(String code) {
        for (ProductInfoItemType type : ProductInfoItemType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static ProductInfoItemType getTypeByCode(String code) {
        ProductInfoItemType[] values = values();
        for (ProductInfoItemType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ProductInfoItemType[] values = ProductInfoItemType.values();
        for (ProductInfoItemType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
