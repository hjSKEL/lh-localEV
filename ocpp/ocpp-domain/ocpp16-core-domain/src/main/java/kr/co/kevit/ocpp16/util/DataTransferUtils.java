/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 19.
 */
public class DataTransferUtils {
    
    private static DataTransferUtils instance = new DataTransferUtils();

    private Map<String, Map<String,Boolean>> vendorMap = new HashMap<>();

    public static DataTransferUtils getInstance() {
        return instance;
    }

    public void addVendor(String vendor, Map<String, Boolean> message) {
        //
        vendorMap.put(vendor, message);
    }
    
    public boolean existVendorId(String vendorId) {
        //
        Map<String,Boolean> messageMap = vendorMap.get(vendorId);
        return messageMap != null;
    }
    
    public Boolean isSupportedMessageId(String vendorId, String messageId) {
        //
        Map<String,Boolean> messageMap = vendorMap.get(vendorId);
        
        if(messageMap == null) {
            return null;
        }
        return messageMap.get(messageId);
    }
}
