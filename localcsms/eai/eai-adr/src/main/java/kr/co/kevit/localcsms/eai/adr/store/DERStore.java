/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.store;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.ocpp201.domain.ChargingScheduleUpdateType;
import kr.co.kevit.ocpp201.domain.DERCurvePointsType;
import kr.co.kevit.ocpp201.domain.DERCurveType;
import kr.co.kevit.ocpp201.enumtype.DERControlEnumType;
import kr.co.kevit.ocpp201.enumtype.DERUnitEnumType;
import kr.co.kevit.ocpp201.request.SetDERControl;
import kr.co.kevit.ocpp201.request.UpdateDynamicSchedule;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2025. 1. 13.
 */
public class DERStore {

    private static DERStore instance = new DERStore();

    private Integer controlId = 0;

    /**
     * NONE , START , ING, FINISH
     */
    public static String der_status = "NONE";
    
    private SetDERControl derControl;
    
    private Integer chargingProfileId = 0;
    
    /**
     * NONE , START , ING, FINISH
     */
    public static String dynaic_status = "NONE";
    
    private UpdateDynamicSchedule dynamicSchedule;

    public DERStore() {
        // Nothing....
    }

    public static DERStore getInstance() {
        //
        return instance;
    }

    public SetDERControl makeSetDERControl() {
        //
        ++controlId;

        SetDERControl request = new SetDERControl();
        request.setControlId(controlId.toString());
        request.setControlType(DERControlEnumType.VoltWatt);
        DERCurveType curve = new DERCurveType();
        curve.setPriority(10);
        curve.setDuration(3000.0);
        curve.setStartTime("2025-01-13T08:00:00");
        curve.setyUnit(DERUnitEnumType.PctMaxW);
        List<DERCurvePointsType> curveData = new ArrayList<>();
        DERCurvePointsType e1 = new DERCurvePointsType();
        e1.setX(0.0);
        e1.setY(100.0);
        curveData.add(e1);
        DERCurvePointsType e2 = new DERCurvePointsType();
        e2.setX(220.0);
        e2.setY(100.0);
        curveData.add(e2);
        DERCurvePointsType e3 = new DERCurvePointsType();
        e3.setX(230.0);
        e3.setY(100.0);
        curveData.add(e3);
        DERCurvePointsType e4 = new DERCurvePointsType();
        e4.setX(240.0);
        e4.setY(50.0);
        curveData.add(e4);
        DERCurvePointsType e5 = new DERCurvePointsType();
        e5.setX(250.0);
        e5.setY(0.0);
        curveData.add(e5);
        curve.setCurveData(curveData);
        request.setCurve(curve);
        return request;
    }

    public SetDERControl getSetDERControl() {
        //
        return this.derControl;
    }

    public UpdateDynamicSchedule makeUpdateDynamicSchedule() {
        //
        UpdateDynamicSchedule request = new UpdateDynamicSchedule();
        request.setChargingProfileId(++chargingProfileId);
        ChargingScheduleUpdateType scheduleUpdate = new ChargingScheduleUpdateType();
        scheduleUpdate.setSetpoint(-10.0);
        scheduleUpdate.setSetpoint_L2(-10.0);
        scheduleUpdate.setSetpoint_L3(-10.0);
        request.setScheduleUpdate(scheduleUpdate);
        return request;
    }
    
    public UpdateDynamicSchedule getUpdateDynamicSchedule() {
        //
        return this.dynamicSchedule;
    }

}
