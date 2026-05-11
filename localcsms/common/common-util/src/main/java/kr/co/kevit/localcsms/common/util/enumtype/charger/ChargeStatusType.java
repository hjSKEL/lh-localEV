package kr.co.kevit.localcsms.common.util.enumtype.charger;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 채널..
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 * @since 2018. 5. 9.
 */
public enum ChargeStatusType implements EnumInterface {

    UNKNOW("CHRS01", "알수없음"),
    TELCOM_ERR("CHRS02", "통신이상"),
    STANDBY("CHRS03", "충전대기"),
    CHARGING("CHRS04", "충전중"),
    STOP("CHRS05", "운영중지"),
    REPAIRING("CHRS06", "점검중"),
    RESERVATION("CHRS07", "예약중"),
    STANDBY4USE("CHRS08", "대기(이용대기)"),
    COMPLETE("CHRS09", "충전완료");

    private String code;

    private String desc;

    ChargeStatusType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static ChargeStatusType getChannelType(String code) {
        for (ChargeStatusType type : ChargeStatusType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static ChargeStatusType getTypeByCode(String code) {
        ChargeStatusType[] values = values();
        for (ChargeStatusType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static List<EnumKeyValue> getKeyValues() {
        List<EnumKeyValue> nameValues = new ArrayList<>();

        ChargeStatusType[] values = ChargeStatusType.values();
        for (ChargeStatusType value : values) {
            nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
        }

        return nameValues;
    }
}
