/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.license;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 12. 4.
 */
public class Vendor {
    
    private static Vendor instance = new Vendor();
    
    private Map<String, String> map = new HashMap<>();
    
    private String remotePort = "22023";
    
    private String ocppPort = "22022";
    
    private boolean isTLS = false;
    
    private boolean isChangeable = false;
    
    public static Vendor getInstance() {
        return instance;
    }
    
    public void add(String vendor, String postFix) {
        map.put(vendor, postFix);
    }
    
    public void setPort(String ocpp, String remote) {
        this.remotePort = remote;
        this.ocppPort = ocpp;
    }
    
    public String getBeanName(String vendor, String messageId) {
        //
        String postFix = map.get(vendor);
        if(postFix == null) {
            return null;
        }
        return messageId + postFix;
    }

    public String remotePort() {
        return this.remotePort;
    }
    
    public String ocppPort() {
        return this.ocppPort;
    }

    /**
     * Get isTLS
     * @return isTLS
     */
    public boolean isTLS() {
        return isTLS;
    }

    /**
     * Set isTLS
     * @param isTLS
     */
    public void setTLS(boolean isTLS) {
        this.isTLS = isTLS;
    }
    
    /**
     * Get isChangeable
     * @return isChangeable
     */
    public boolean isChangeable() {
        return isChangeable;
    }

    /**
     * Set isChangeable
     * @param isChangeable
     */
    public void setChangeable(boolean isChangeable) {
        this.isChangeable = isChangeable;
    }
    
}
