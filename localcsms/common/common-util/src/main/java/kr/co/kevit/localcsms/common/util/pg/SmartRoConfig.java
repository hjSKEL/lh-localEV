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
public class SmartRoConfig {
    
    private Map<String, Map<String, String>> config;
    
    private static SmartRoConfig instance = new SmartRoConfig();
    
    public SmartRoConfig() {
        //
        config = new HashMap<String, Map<String, String>>();
        
        Map<String, String> kcConfig = new HashMap<>();
        kcConfig.put(PgConstants.SspMallID, "KCIEVC000p");
        kcConfig.put(PgConstants.MID1, "kcievc001m");
        kcConfig.put(PgConstants.MID1CANCELPW, "191031");
        kcConfig.put(PgConstants.MERCHANTKEY1, "+sjRHahPe2GfZCjcI11KCXIs/pk751EGF6PVJ+5UpHEKFi0STN6nOZbaqt6+/g3LrerDd17KqLw/WNyp3V+1Ew==");
        
        kcConfig.put(PgConstants.MID2, "kcievc002m");
        kcConfig.put(PgConstants.MID2CANCELPW, "191031");
        kcConfig.put(PgConstants.MERCHANTKEY2, "6pM2hC1P++2Ea/sJCqLBpvQmcDbNByLbBBuUHTFeGMnorPhACeWqXVy2FnQLhNe5L+fXMte+XsMiLGIsOdK3Uw==");
        config.put("KC", kcConfig);
        
        Map<String, String> f1Config = new HashMap<>();
        f1Config.put(PgConstants.SspMallID, "KEVIT0000p");
        f1Config.put(PgConstants.MID1, "kevit0001m");
        f1Config.put(PgConstants.MID1CANCELPW, "082400");
        f1Config.put(PgConstants.MERCHANTKEY1, "C3PtGKk/eQrH/Qen3mMypWTHMN1Eaalu6FhkZz5nzWt5D4EJ0DI1Vq4elvUBUCek5oCQwbMAsERZozPHKrPKUw==");
        
        f1Config.put(PgConstants.MID2, "kevit0002m");
        f1Config.put(PgConstants.MID2CANCELPW, "000617");
        f1Config.put(PgConstants.MERCHANTKEY2, "nIzfWE3IVJvRc+Ilu0MbAnjRgxkxqlMUBFIhBm2Aj9Zen6aB0G0Dmh7/X8lj8PVOZGRsMbLYD5g98cTws2Nplw==");
        config.put("F1", f1Config);
    }
    
    public static SmartRoConfig getInstance() {
        //
        if(instance == null) {
            instance = new SmartRoConfig();
        }
        return instance;
    }
    
    public String getValue(String bid, String key) {
        //
        return config.get(bid).get(key);
    }
}
