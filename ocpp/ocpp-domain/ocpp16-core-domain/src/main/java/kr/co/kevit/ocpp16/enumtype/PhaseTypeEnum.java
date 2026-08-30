/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public enum PhaseTypeEnum {
    L1,
    L2,
    L3,
    N,
    @SerializedName("L1-N")
    L1_N,
    @SerializedName("L2-N")
    L2_N,
    @SerializedName("L3-N")
    L3_N,
    @SerializedName("L1-L2")
    L1_L2,
    @SerializedName("L2-L3")
    L2_L3,
    @SerializedName("L3-L1")
    L3_L1
}
