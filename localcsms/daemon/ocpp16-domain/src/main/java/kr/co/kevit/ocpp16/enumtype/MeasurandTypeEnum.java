/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 6. 26.
 */
public enum MeasurandTypeEnum {
    
    @SerializedName("Energy.Active.Export.Register")
    Energy_Active_Export_Register,
    @SerializedName("Energy.Active.Import.Register")
    Energy_Active_Import_Register,
    @SerializedName("Energy.Reactive.Export.Register")
    Energy_Reactive_Export_Register,
    @SerializedName("Energy.Reactive.Import.Register")
    Energy_Reactive_Import_Register,
    @SerializedName("Energy.Active.Export.Interval")
    Energy_Active_Export_Interval,
    @SerializedName("Energy.Active.Import.Interval")
    Energy_Active_Import_Interval,
    @SerializedName("Energy.Reactive.Export.Interval")
    Energy_Reactive_Export_Interval,
    @SerializedName("Energy.Reactive.Import.Interval")
    Energy_Reactive_Import_Interval,
    @SerializedName("Power.Active.Export")
    Power_Active_Export,
    @SerializedName("Power.Active.Import")
    Power_Active_Import,
    @SerializedName("Power.Offered")
    Power_Offered,
    @SerializedName("Power.Reactive.Export")
    Power_Reactive_Export,
    @SerializedName("Power.Reactive.Import")
    Power_Reactive_Import,
    @SerializedName("Power.Factor")
    Power_Factor,
    @SerializedName("Current.Import")
    Current_Import,
    @SerializedName("Current.Export")
    Current_Export,
    @SerializedName("Current.Offered")
    Current_Offered,
    @SerializedName("Energy.Active.Import.Instant")
    Energy_Active_Import_Instant,
    Voltage,
    Frequency,
    Temperature,
    SoC,
    RPM,
    RemainingTime, //kevit
    RemainingKwh,  //kevit
}
