/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp16.util;

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
        map.put(kr.co.kevit.ocpp16.request.DataTransfer.class.getSimpleName(), Boolean.TRUE);
        
        //
        map.put(kr.co.kevit.ocpp16.request.Authorize.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.BootNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.Heartbeat.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.StartTransaction.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.StopTransaction.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.MeterValues.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.DiagnosticsStatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.FirmwareStatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.StatusNotification.class.getSimpleName(), Boolean.TRUE);
        
        map.put(kr.co.kevit.ocpp16.request.SignCertificate.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.SecurityEventNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.LogStatusNotification.class.getSimpleName(), Boolean.TRUE);
        map.put(kr.co.kevit.ocpp16.request.SignedFirmwareStatusNotification.class.getSimpleName(), Boolean.TRUE);
        
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
