package kr.co.kevit.localcsms.common.util.enumtype.outbound;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 *
 */
public enum SMSMessageType implements EnumInterface {
    //
    CUST_CHARGE_COMPLETE("SMS030", "개인-충전완료"),
    BIZ_CHARGE_COMPLETE("SMS020", "법인-충전완료"),
    CHARGER_REPAIR_NOTICE("SMS038", "충전기 정비 안내")
    ;

    SMSMessageType(String code, String desc) {
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

    public static SMSMessageType getTypeByCode(String code) {
        SMSMessageType[] values = values();
        for (SMSMessageType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        SMSMessageType[] values = SMSMessageType.values();
        for (SMSMessageType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}