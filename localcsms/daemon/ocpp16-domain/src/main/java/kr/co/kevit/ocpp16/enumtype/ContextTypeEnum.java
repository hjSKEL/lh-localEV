/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.enumtype;

import com.google.gson.annotations.SerializedName;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public enum ContextTypeEnum {
    
    @JsonProperty("Interruption.Begin") @SerializedName("Interruption.Begin")
    Interruption_Begin,
    @JsonProperty("Interruption.End") @SerializedName("Interruption.End")
    Interruption_End,
    @JsonProperty("Sample.Clock") @SerializedName("Sample.Clock")
    Sample_Clock,
    @JsonProperty("Sample.Periodic") @SerializedName("Sample.Periodic")
    Sample_Periodic,
    @JsonProperty("Transaction.Begin") @SerializedName("Transaction.Begin")
    Transaction_Begin,
    @JsonProperty("Transaction.End") @SerializedName("Transaction.End")
    Transaction_End,
    Trigger,
    Other;
}
