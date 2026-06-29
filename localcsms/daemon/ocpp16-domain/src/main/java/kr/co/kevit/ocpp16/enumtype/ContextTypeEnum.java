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
public enum ContextTypeEnum {
    
    @SerializedName("Interruption.Begin")
    Interruption_Begin,
    @SerializedName("Interruption.End")
    Interruption_End,
    @SerializedName("Sample.Clock")
    Sample_Clock,
    @SerializedName("Sample.Periodic")
    Sample_Periodic,
    @SerializedName("Transaction.Begin")
    Transaction_Begin,
    @SerializedName("Transaction.End")
    Transaction_End,
    Trigger,
    Other;
}
