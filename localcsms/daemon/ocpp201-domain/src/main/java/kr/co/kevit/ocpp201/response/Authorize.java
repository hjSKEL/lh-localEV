/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.response;

import java.util.List;
import java.util.Map;

import kr.co.kevit.ocpp201.domain.IdTokenInfoType;
import kr.co.kevit.ocpp201.domain.TariffType;
import kr.co.kevit.ocpp201.enumtype.AuthorizeCertificateStatusEnumType;
import kr.co.kevit.ocpp201.enumtype.EnergyTransferModeEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class Authorize {
    
    /**
     * required
     */
    private IdTokenInfoType idTokenInfo;
    
    /**
     * 
     */
    private AuthorizeCertificateStatusEnumType certificateStatus;

    /**
     * (2.1)
     */
    private List<EnergyTransferModeEnumType> allowedEnergyTransfer;

    /**
     * (2.1)
     */
    private TariffType tariff;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public IdTokenInfoType getIdTokenInfo() {
        return idTokenInfo;
    }

    public void setIdTokenInfo(IdTokenInfoType idTokenInfo) {
        this.idTokenInfo = idTokenInfo;
    }

    public AuthorizeCertificateStatusEnumType getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(AuthorizeCertificateStatusEnumType certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public List<EnergyTransferModeEnumType> getAllowedEnergyTransfer() {
        return allowedEnergyTransfer;
    }

    public void setAllowedEnergyTransfer(List<EnergyTransferModeEnumType> allowedEnergyTransfer) {
        this.allowedEnergyTransfer = allowedEnergyTransfer;
    }

    public TariffType getTariff() {
        return tariff;
    }

    public void setTariff(TariffType tariff) {
        this.tariff = tariff;
    }

}