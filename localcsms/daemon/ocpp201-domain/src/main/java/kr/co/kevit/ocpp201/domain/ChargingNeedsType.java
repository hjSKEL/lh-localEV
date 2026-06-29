/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.domain;

import kr.co.kevit.ocpp201.enumtype.ControlModeEnumType;
import kr.co.kevit.ocpp201.enumtype.EnergyTransferModeEnumType;
import kr.co.kevit.ocpp201.enumtype.MobilityNeedsModeEnumType;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 15.
 */
public class ChargingNeedsType {
    
    private ACChargingParametersType acChargingParameters;
    
    private DCChargingParametersType dcChargingParameters;

    private EVEnergyOfferType evEnergyOffer;

    private DERChargingParametersType derChargingParameters;

    /**
     * required
     */
    private EnergyTransferModeEnumType requestedEnergyTransfer;

    private V2XChargingParametersType v2xChargingParameters;

    /**
     * *(2.1)* Modes of energy transfer that are marked as available by EV.
     */
    private List<EnergyTransferModeEnumType> availableEnergyTransfer;
    
    /**
     * "Estimated departure time of the EV.
     * ISO 15118-2:* AC/DC_EVChargeParameterType: DepartureTime
     * ISO 15118-20:* Dynamic/Scheduled_SEReqControlModeType: DepartureTIme
     */
    private String departureTime;

    private ControlModeEnumType controlMode;

    private MobilityNeedsModeEnumType mobilityNeedsMode;

    private Map<String, Object> customData;

    public ACChargingParametersType getAcChargingParameters() {
        return acChargingParameters;
    }

    public void setAcChargingParameters(ACChargingParametersType acChargingParameters) {
        this.acChargingParameters = acChargingParameters;
    }

    public DCChargingParametersType getDcChargingParameters() {
        return dcChargingParameters;
    }

    public void setDcChargingParameters(DCChargingParametersType dcChargingParameters) {
        this.dcChargingParameters = dcChargingParameters;
    }

    public EVEnergyOfferType getEvEnergyOffer() {
        return evEnergyOffer;
    }

    public void setEvEnergyOffer(EVEnergyOfferType evEnergyOffer) {
        this.evEnergyOffer = evEnergyOffer;
    }

    public DERChargingParametersType getDerChargingParameters() {
        return derChargingParameters;
    }

    public void setDerChargingParameters(DERChargingParametersType derChargingParameters) {
        this.derChargingParameters = derChargingParameters;
    }

    public EnergyTransferModeEnumType getRequestedEnergyTransfer() {
        return requestedEnergyTransfer;
    }

    public void setRequestedEnergyTransfer(EnergyTransferModeEnumType requestedEnergyTransfer) {
        this.requestedEnergyTransfer = requestedEnergyTransfer;
    }

    public V2XChargingParametersType getV2xChargingParameters() {
        return v2xChargingParameters;
    }

    public void setV2xChargingParameters(V2XChargingParametersType v2xChargingParameters) {
        this.v2xChargingParameters = v2xChargingParameters;
    }

    public List<EnergyTransferModeEnumType> getAvailableEnergyTransfer() {
        return availableEnergyTransfer;
    }

    public void setAvailableEnergyTransfer(List<EnergyTransferModeEnumType> availableEnergyTransfer) {
        this.availableEnergyTransfer = availableEnergyTransfer;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public ControlModeEnumType getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlModeEnumType controlMode) {
        this.controlMode = controlMode;
    }

    public MobilityNeedsModeEnumType getMobilityNeedsMode() {
        return mobilityNeedsMode;
    }

    public void setMobilityNeedsMode(MobilityNeedsModeEnumType mobilityNeedsMode) {
        this.mobilityNeedsMode = mobilityNeedsMode;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
