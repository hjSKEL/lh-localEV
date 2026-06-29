/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

import com.google.gson.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public enum PhaseEnumType {
    L1,
    L2,
    L3,
    N,
    @JsonProperty("L1-N") @SerializedName("L1-N")
    L1_N,
    @JsonProperty("L2-N") @SerializedName("L2-N")
    L2_N,
    @JsonProperty("L3-N") @SerializedName("L3-N")
    L3_N,
    @JsonProperty("L1-L2") @SerializedName("L1-L2")
    L1_L2,
    @JsonProperty("L2-L3") @SerializedName("L2-L3")
    L2_L3,
    @JsonProperty("L3-L1") @SerializedName("L3-L1")
    L3_L1
}
