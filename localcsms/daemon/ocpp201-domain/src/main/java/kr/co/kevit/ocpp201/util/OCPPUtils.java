/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp201.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 2. 21.
 */
public class OCPPUtils {

    private static OCPPUtils instance = new OCPPUtils();

    private Map<String, Boolean> map = new HashMap<>();

    public static OCPPUtils getInstance() {
        return instance;
    }

    public OCPPUtils() {
        // CS to CSMS
        map.put(kr.co.kevit.ocpp201.request.DataTransfer.class.getSimpleName(), Boolean.TRUE);
        
        map.put(kr.co.kevit.ocpp201.request.Authorize.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.BootNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.TransactionEvent.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.Heartbeat.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.StatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.MeterValues.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyEvent.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.FirmwareStatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.PublishFirmwareStatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyReport.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyCustomerInformation.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyDisplayMessages.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.LogStatusNotification.class.getSimpleName(), Boolean.TRUE);
        
        map.put(kr.co.kevit.ocpp201.request.SignCertificate.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.SecurityEventNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyMonitoringReport.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.ReservationStatusUpdate.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyChargingLimit.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.ReportChargingProfiles.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyEVChargingNeeds.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyEVChargingSchedule.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.ClearedChargingLimit.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.Get15118EVCertificate.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.GetCertificateStatus.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.SetDERControl.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyDERAlarm.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.GetDERControl.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.NotifyDERStartStop.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.ClearDERControl.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp201.request.UpdateDynamicSchedule.class.getSimpleName(), Boolean.TRUE);

        // CSMS to CS 
        // Undefined
    }

    public Boolean isRegistedType(String command) {
        //
        return map.get(command) != null;
    }

    public Boolean isSupportedType(String command) {
        //
        Boolean bool = map.get(command);
        return bool == null ? Boolean.FALSE : bool;
    }
}
