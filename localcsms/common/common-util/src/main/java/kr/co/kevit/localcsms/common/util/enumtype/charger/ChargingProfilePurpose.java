package kr.co.kevit.localcsms.common.util.enumtype.charger;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * OCPP 2.x ChargingProfilePurposeEnumType → 공통코드 CHPP00
 */
public enum ChargingProfilePurpose implements EnumInterface {

    ChargingStationMaxProfile    ("CHPP01", "ChargingStationMaxProfile"),
    TxDefaultProfile             ("CHPP02", "TxDefaultProfile"),
    TxProfile                    ("CHPP03", "TxProfile"),
    PriorityCharging             ("CHPP04", "PriorityCharging"),
    LocalGeneration              ("CHPP05", "LocalGeneration"),
    ChargingStationExternalConstraints("CHPP06", "ChargingStationExternalConstraints");

    private final String code;
    private final String desc;

    ChargingProfilePurpose(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getDesc() { return desc; }

    public static ChargingProfilePurpose getTypeByCode(String code) {
        if (code == null) return null;
        for (ChargingProfilePurpose v : values()) {
            if (v.code.equals(code)) return v;
        }
        return null;
    }
}
