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
public enum MeasurandEnumType {
    
    @JsonProperty("Current.Export") @SerializedName("Current.Export")
    Current_Export,
    @JsonProperty("Current.Import") @SerializedName("Current.Import")
    Current_Import,
    @JsonProperty("Current.Offered") @SerializedName("Current.Offered")
    Current_Offered,
    @JsonProperty("Energy.Active.Export.Register") @SerializedName("Energy.Active.Export.Register")
    Energy_Active_Export_Register,
    @JsonProperty("Energy.Active.Import.Register") @SerializedName("Energy.Active.Import.Register")
    Energy_Active_Import_Register,
    @JsonProperty("Energy.Reactive.Export.Register") @SerializedName("Energy.Reactive.Export.Register")
    Energy_Reactive_Export_Register,
    @JsonProperty("Energy.Reactive.Import.Register") @SerializedName("Energy.Reactive.Import.Register")
    Energy_Reactive_Import_Register,
    @JsonProperty("Energy.Active.Export.Interval") @SerializedName("Energy.Active.Export.Interval")
    Energy_Active_Export_Interval,
    @JsonProperty("Energy.Active.Import.Interval") @SerializedName("Energy.Active.Import.Interval")
    Energy_Active_Import_Interval,
    @JsonProperty("Energy.Active.Net") @SerializedName("Energy.Active.Net")
    Energy_Active_Net,
    @JsonProperty("Energy.Reactive.Export.Interval") @SerializedName("Energy.Reactive.Export.Interval")
    Energy_Reactive_Export_Interval,
    @JsonProperty("Energy.Reactive.Import.Interval") @SerializedName("Energy.Reactive.Import.Interval")
    Energy_Reactive_Import_Interval,
    @JsonProperty("Energy.Reactive.Net") @SerializedName("Energy.Reactive.Net")
    Energy_Reactive_Net,
    @JsonProperty("Energy.Apparent.Net") @SerializedName("Energy.Apparent.Net")
    Energy_Apparent_Net,
    @JsonProperty("Energy.Apparent.Import") @SerializedName("Energy.Apparent.Import")
    Energy_Apparent_Import,
    @JsonProperty("Energy.Apparent.Export") @SerializedName("Energy.Apparent.Export")
    Energy_Apparent_Export,
    Frequency,
    @JsonProperty("Power.Active.Export") @SerializedName("Power.Active.Export")
    Power_Active_Export,
    @JsonProperty("Power.Active.Import") @SerializedName("Power.Active.Import")
    Power_Active_Import,
    @JsonProperty("Power.Factor") @SerializedName("Power.Factor")
    Power_Factor,
    @JsonProperty("Power.Offered") @SerializedName("Power.Offered")
    Power_Offered,
    @JsonProperty("Power.Reactive.Export") @SerializedName("Power.Reactive.Export")
    Power_Reactive_Export,
    @JsonProperty("Power.Reactive.Import") @SerializedName("Power.Reactive.Import")
    Power_Reactive_Import,
    SoC,
    Voltage,
    // (2.1)
    @JsonProperty("Current.Export.Offered") @SerializedName("Current.Export.Offered")
    Current_Export_Offered,
    @JsonProperty("Current.Export.Minimum") @SerializedName("Current.Export.Minimum")
    Current_Export_Minimum,
    @JsonProperty("Current.Import.Offered") @SerializedName("Current.Import.Offered")
    Current_Import_Offered,
    @JsonProperty("Current.Import.Minimum") @SerializedName("Current.Import.Minimum")
    Current_Import_Minimum,
    @JsonProperty("Display.PresentSOC") @SerializedName("Display.PresentSOC")
    Display_PresentSOC,
    @JsonProperty("Display.MinimumSOC") @SerializedName("Display.MinimumSOC")
    Display_MinimumSOC,
    @JsonProperty("Display.TargetSOC") @SerializedName("Display.TargetSOC")
    Display_TargetSOC,
    @JsonProperty("Display.MaximumSOC") @SerializedName("Display.MaximumSOC")
    Display_MaximumSOC,
    @JsonProperty("Display.RemainingTimeToMinimumSOC") @SerializedName("Display.RemainingTimeToMinimumSOC")
    Display_RemainingTimeToMinimumSOC,
    @JsonProperty("Display.RemainingTimeToTargetSOC") @SerializedName("Display.RemainingTimeToTargetSOC")
    Display_RemainingTimeToTargetSOC,
    @JsonProperty("Display.RemainingTimeToMaximumSOC") @SerializedName("Display.RemainingTimeToMaximumSOC")
    Display_RemainingTimeToMaximumSOC,
    @JsonProperty("Display.ChargingComplete") @SerializedName("Display.ChargingComplete")
    Display_ChargingComplete,
    @JsonProperty("Display.BatteryEnergyCapacity") @SerializedName("Display.BatteryEnergyCapacity")
    Display_BatteryEnergyCapacity,
    @JsonProperty("Display.InletHot") @SerializedName("Display.InletHot")
    Display_InletHot,
    @JsonProperty("Energy.Active.Import.CableLoss") @SerializedName("Energy.Active.Import.CableLoss")
    Energy_Active_Import_CableLoss,
    @JsonProperty("Energy.Active.Import.LocalGeneration.Register") @SerializedName("Energy.Active.Import.LocalGeneration.Register")
    Energy_Active_Import_LocalGeneration_Register,
    @JsonProperty("Energy.Active.Setpoint.Interval") @SerializedName("Energy.Active.Setpoint.Interval")
    Energy_Active_Setpoint_Interval,
    @JsonProperty("EnergyRequest.Target") @SerializedName("EnergyRequest.Target")
    EnergyRequest_Target,
    @JsonProperty("EnergyRequest.Minimum") @SerializedName("EnergyRequest.Minimum")
    EnergyRequest_Minimum,
    @JsonProperty("EnergyRequest.Maximum") @SerializedName("EnergyRequest.Maximum")
    EnergyRequest_Maximum,
    @JsonProperty("EnergyRequest.Minimum.V2X") @SerializedName("EnergyRequest.Minimum.V2X")
    EnergyRequest_Minimum_V2X,
    @JsonProperty("EnergyRequest.Maximum.V2X") @SerializedName("EnergyRequest.Maximum.V2X")
    EnergyRequest_Maximum_V2X,
    @JsonProperty("EnergyRequest.Bulk") @SerializedName("EnergyRequest.Bulk")
    EnergyRequest_Bulk,
    @JsonProperty("Power.Active.Setpoint") @SerializedName("Power.Active.Setpoint")
    Power_Active_Setpoint,
    @JsonProperty("Power.Active.Residual") @SerializedName("Power.Active.Residual")
    Power_Active_Residual,
    @JsonProperty("Power.Export.Minimum") @SerializedName("Power.Export.Minimum")
    Power_Export_Minimum,
    @JsonProperty("Power.Export.Offered") @SerializedName("Power.Export.Offered")
    Power_Export_Offered,
    @JsonProperty("Power.Import.Offered") @SerializedName("Power.Import.Offered")
    Power_Import_Offered,
    @JsonProperty("Power.Import.Minimum") @SerializedName("Power.Import.Minimum")
    Power_Import_Minimum,
    @JsonProperty("Voltage.Minimum") @SerializedName("Voltage.Minimum")
    Voltage_Minimum,
    @JsonProperty("Voltage.Maximum") @SerializedName("Voltage.Maximum")
    Voltage_Maximum
}
