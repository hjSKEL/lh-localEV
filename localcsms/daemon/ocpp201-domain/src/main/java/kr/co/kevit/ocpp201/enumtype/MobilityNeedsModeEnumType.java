/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.enumtype;

/**
 * *(2.1)* Value of EVCC indicates that EV determines min/target SOC and departure time.
 * A value of EVCC_SECC indicates that charging station or CSMS may also update min/target SOC and departure time.
 * *ISO 15118-20:* +\r\nServiceSelectionReq(SelectedEnergyTransferService)
 * @author jhkim <a href=mailto:jhkim@kevit.co.kr>jhkim@kevit.co.kr</a>
 * @since 2024. 10. 22.
 */
public enum MobilityNeedsModeEnumType {
    EVCC,
    EVCC_SECC
}
