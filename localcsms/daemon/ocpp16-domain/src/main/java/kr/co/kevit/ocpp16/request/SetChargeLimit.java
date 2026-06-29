/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp16.request;

/**
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2024. 8. 1.
 */
public class SetChargeLimit {
    
    /**
     * required
     */
    private int soc;
    /**
     * required
     */
    private int time;

    private Integer transactionId;

    private double kwh;

    /**
     * 베터리정보전송동의여부
     * Y : 동의,  N : 미동의
     */
    private String batAgreeYn;

    public int getSoc() {
        return soc;
    }

    public void setSoc(int soc) {
        this.soc = soc;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public double getKwh() {
        return kwh;
    }

    public void setKwh(double kwh) {
        this.kwh = kwh;
    }

    public String getBatAgreeYn() {
        return batAgreeYn;
    }

    public void setBatAgreeYn(String batAgreeYn) {
        this.batAgreeYn = batAgreeYn;
    }
}
