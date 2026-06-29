/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2025. 6. 19.
 */
public class InstallKecoCert {
    
    /**
     * required
     */
    private String url;
    /**
     * required
     */
    private String hash;
    /**
     * required
     */
    private String kecoCpCsId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKecoCpCsId() {
        return kecoCpCsId;
    }

    public void setKecoCpCsId(String kecoCpCsId) {
        this.kecoCpCsId = kecoCpCsId;
    }
}
