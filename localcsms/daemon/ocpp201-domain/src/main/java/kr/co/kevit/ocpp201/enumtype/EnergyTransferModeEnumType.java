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
public enum EnergyTransferModeEnumType {
    DC,
    AC_single_phase,
    AC_two_phase,
    AC_three_phase,
    // (2.1)
    AC_BPT,
    AC_BPT_DER,
    AC_DER,
    DC_BPT,
    DC_ACDP,
    DC_ACDP_BPT,
    WPT
}
