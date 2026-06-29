/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * Result of operation.
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public enum DERControlStatusEnumType {
    Accepted,
    Rejected,
    Unsupported,
    NotFound,
    // (2.1)
    NotSupported
}
