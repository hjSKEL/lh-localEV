/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * Parameter is only sent, if the EV has to feed-in power or reactive power during fault-ride through (FRT) as defined by HVMomCess curve and LVMomCess curve.
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 21.
 */
public enum PowerDuringCessationEnumType {
    Active,
    Reactive
}
