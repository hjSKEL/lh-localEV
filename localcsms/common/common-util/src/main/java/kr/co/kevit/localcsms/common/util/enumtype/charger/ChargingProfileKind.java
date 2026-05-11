package kr.co.kevit.localcsms.common.util.enumtype.charger;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * OCPP 2.x ChargingProfileKindEnumType → 공통코드 CHKD00
 */
public enum ChargingProfileKind implements EnumInterface {

    Absolute  ("CHKD01", "Absolute"),
    Recurring ("CHKD02", "Recurring"),
    Relative  ("CHKD03", "Relative");

    private final String code;
    private final String desc;

    ChargingProfileKind(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getDesc() { return desc; }

    public static ChargingProfileKind getTypeByCode(String code) {
        if (code == null) return null;
        for (ChargingProfileKind v : values()) {
            if (v.code.equals(code)) return v;
        }
        return null;
    }
}
