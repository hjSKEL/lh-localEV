/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * 
 * @author bckim <a href=mailto:bckim@kevit.co.kr>bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public enum DisplayMessageStatusEnumType {
    Accepted,
    NotSupportedMessageFormat,
    Rejected,
    NotSupportedPriority,
    NotSupportedState,
    UnknownTransaction,
    // (2.1)
    LanguageNotSupported
}
