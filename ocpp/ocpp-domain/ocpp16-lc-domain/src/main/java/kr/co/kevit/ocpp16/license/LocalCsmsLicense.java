/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.license;

import kr.co.kevit.ocpp16.license.Vendor;
import kr.co.kevit.ocpp16.util.DataTransferUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 12. 4.
 */
public class LocalCsmsLicense {

    public LocalCsmsLicense() {
        //
        Vendor.getInstance().setPort("9000", "22012");
        Vendor.getInstance().setTLS(false);
        Vendor.getInstance().setChangeable(true);
        
        String kevitVendor = "kr.co.kevit";
        Vendor.getInstance().add(kevitVendor,"_LC");
        Map<String, Boolean> messageMap  = new HashMap<>();
        messageMap.put("FixedTariff", true);
        DataTransferUtils.getInstance().addVendor(kevitVendor, messageMap);
    }
    
    public boolean validate() {
        //
        return true;
    }
}
