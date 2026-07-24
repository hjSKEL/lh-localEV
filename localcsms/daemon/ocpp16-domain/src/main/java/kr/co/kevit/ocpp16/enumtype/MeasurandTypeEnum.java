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
public enum MeasurandTypeEnum {

    @JsonProperty("Energy.Active.Export.Register")
    @SerializedName("Energy.Active.Export.Register")
    Energy_Active_Export_Register,
    @JsonProperty("Energy.Active.Import.Register")
    @SerializedName("Energy.Active.Import.Register")
    Energy_Active_Import_Register,
    @JsonProperty("Energy.Reactive.Export.Register")
    @SerializedName("Energy.Reactive.Export.Register")
    Energy_Reactive_Export_Register,
    @JsonProperty("Energy.Reactive.Import.Register")
    @SerializedName("Energy.Reactive.Import.Register")
    Energy_Reactive_Import_Register,
    @JsonProperty("Energy.Active.Export.Interval")
    @SerializedName("Energy.Active.Export.Interval")
    Energy_Active_Export_Interval,
    @JsonProperty("Energy.Active.Import.Interval")
    @SerializedName("Energy.Active.Import.Interval")
    Energy_Active_Import_Interval,
    @JsonProperty("Energy.Reactive.Export.Interval")
    @SerializedName("Energy.Reactive.Export.Interval")
    Energy_Reactive_Export_Interval,
    @JsonProperty("Energy.Reactive.Import.Interval")
    @SerializedName("Energy.Reactive.Import.Interval")
    Energy_Reactive_Import_Interval,
    @JsonProperty("Power.Active.Export")
    @SerializedName("Power.Active.Export")
    Power_Active_Export,
    @JsonProperty("Power.Active.Import")
    @SerializedName("Power.Active.Import")
    Power_Active_Import,
    @JsonProperty("Power.Offered")
    @SerializedName("Power.Offered")
    Power_Offered,
    @JsonProperty("Power.Reactive.Export")
    @SerializedName("Power.Reactive.Export")
    Power_Reactive_Export,
    @JsonProperty("Power.Reactive.Import")
    @SerializedName("Power.Reactive.Import")
    Power_Reactive_Import,
    @JsonProperty("Power.Factor")
    @SerializedName("Power.Factor")
    Power_Factor,
    @JsonProperty("Current.Import")
    @SerializedName("Current.Import")
    Current_Import,
    @JsonProperty("Current.Export")
    @SerializedName("Current.Export")
    Current_Export,
    @JsonProperty("Current.Offered")
    @SerializedName("Current.Offered")
    Current_Offered,
    @JsonProperty("Energy.Active.Import.Instant")
    @SerializedName("Energy.Active.Import.Instant")
    Energy_Active_Import_Instant,
    Voltage,
    Frequency,
    Temperature,
    SoC,
    RPM,
    RemainingTime, // kevit
    RemainingKwh, // kevit
    Fee // HU
}
