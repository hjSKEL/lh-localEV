/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/

import com.google.gson.Gson;
import kr.co.kevit.ocpp201.request.*;
import org.junit.Test;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2022. 9. 16.
 */
public class RemoteBeanTest {

    @Test
    public void test() {
        String json = "{\"requestedMessage\":\"Heartbeat\",\"evse\":{}}";
        System.out.println(makeObject("TriggerMessage", json));
    }
    private static Object makeObject(String actionName, String json) {
        //
        switch (actionName) {
        case "SendLocalList":
            return new Gson().fromJson(json, SendLocalList.class);
        case "GetLocalListVersion":
            return new Gson().fromJson(json, GetLocalListVersion.class);
        case "InstallCertificate":
            return new Gson().fromJson(json, InstallCertificate.class);
        case "GetInstalledCertificateIds":
            return new Gson().fromJson(json, GetInstalledCertificateIds.class);
        case "DeleteCertificate":
            return new Gson().fromJson(json, DeleteCertificate.class);
        case "Reset":
            return new Gson().fromJson(json, Reset.class);
        case "UnlockConnector":
            return new Gson().fromJson(json, UnlockConnector.class);
        case "ChangeAvailability":
            return new Gson().fromJson(json, ChangeAvailability.class);
        case "SetVariables":
            SetVariables changeConfiguration = new Gson().fromJson(json, SetVariables.class);
            return changeConfiguration;
        case "ClearCache":
            return new Gson().fromJson(json, ClearCache.class);
        case "SetChargingProfile":
            return new Gson().fromJson(json, SetChargingProfile.class);
        case "ClearChargingProfile":
            return new Gson().fromJson(json, ClearChargingProfile.class);
        case "GetVariables":
            return new Gson().fromJson(json, GetVariables.class);
        case "DataTransfer":
            return new Gson().fromJson(json, DataTransfer.class);
        case "GetMonitoringReport":
            return new Gson().fromJson(json, GetMonitoringReport.class);
        case "GetLog":
            return new Gson().fromJson(json, GetLog.class);
        case "ReserveNow":
            return new Gson().fromJson(json, ReserveNow.class);
        case "CancelReservation":
            return new Gson().fromJson(json, CancelReservation.class);
        case "GetCompositeSchedule":
            return new Gson().fromJson(json, GetCompositeSchedule.class);
        case "RequestStartTransaction":
            return new Gson().fromJson(json, RequestStartTransaction.class);
        case "RequestStopTransaction":
            return new Gson().fromJson(json, RequestStopTransaction.class);
        case "TriggerMessage":
            return new Gson().fromJson(json, TriggerMessage.class);
        case "UpdateFirmware":
            return new Gson().fromJson(json, UpdateFirmware.class);
        case "SetDisplayMessage":
            return new Gson().fromJson(json, SetDisplayMessage.class);
        }
        return new Object();
    }

}
