/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.daemon.store;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author KEVIT <a href="mailto:"></a> 
 * @since 2023. 1. 16.
 */
public class DaemonStore {
    
    public static Map<String, Date> HeartbeatMap = new HashMap<>();
    
    public static Map<String, Date> MeterValuesMap = new HashMap<>();
    
    public static Map<String, String> StatusNotificationMap = new HashMap<>();
    public static Map<String, Date> StatusNotificationTimeMap = new HashMap<>();

    public static Map<String, String> LogMap = new HashMap<>();
}
