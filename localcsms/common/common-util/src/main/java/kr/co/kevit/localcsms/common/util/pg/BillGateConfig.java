/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.pg;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 8. 18.
 */
public class BillGateConfig {
    
    private Map<String, Map<String, String>> config;
    
    private static BillGateConfig instance = new BillGateConfig();
    
    public BillGateConfig() {
        //
        config = new HashMap<String, Map<String, String>>();
        Map<String, String> keConfig = new HashMap<>();
        keConfig.put(PgConstants.PERS_SERVICE_ID, "TEST0001");
        keConfig.put(PgConstants.BIZ_SERVICE_ID, "TEST0001");
        config.put("KE", keConfig);
    }
    
    public static BillGateConfig getInstance() {
        //
        if(instance == null) {
            instance = new BillGateConfig();
        }
        return instance;
    }
    
    public String getValue(String bid, String key) {
        //
        return config.get(bid).get(key);
    }
}
