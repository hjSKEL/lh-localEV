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
public enum DataEnumType {
    string,
    passwordString,
    decimal,
    integer,
    dateTime,
    @JsonProperty("boolean") @SerializedName("boolean")
    Boolean,
    OptionList,
    SequenceList,
    MemberList
}
