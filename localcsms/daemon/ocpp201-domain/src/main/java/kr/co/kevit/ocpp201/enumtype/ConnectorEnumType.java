/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

import com.google.gson.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author bckim <a href=mailto:bckim@kevit.co.kr>bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public enum ConnectorEnumType {
    cCCS1,
    cCCS2,
    cChaoJi,
    cG105,
    @JsonProperty("cGBT-DC") @SerializedName("cGBT-DC")
    cGBT_DC,
    cLECCS,
    cMCS,
    cNACS,
    @JsonProperty("cNACS") @SerializedName("cNACS")
    cNACS_CCS1,
    cTesla,
    cType1,
    cType2,
    cUltraChaoJi,
    @JsonProperty("s309-1P-16A") @SerializedName("s309-1P-16A")
    s309_1P_16A,
    @JsonProperty("s309-1P-32A") @SerializedName("s309-1P-32A")
    s309_1P_32A,
    @JsonProperty("s309-3P-16A") @SerializedName("s309-3P-16A")
    s309_3P_16A,
    @JsonProperty("s309-3P-32A") @SerializedName("s309-3P-32A")
    s309_3P_32A,
    sBS1361,
    @JsonProperty("sCEE-7-7") @SerializedName("sCEE-7-7")
    sCEE_7_7,
    sType2,
    sType3,
    Other1PhMax16A,
    Other1PhOver16A,
    Other3Ph,
    Pan,
    wInductive,
    wResonant,
    Undetermined,
    Unknown
}
