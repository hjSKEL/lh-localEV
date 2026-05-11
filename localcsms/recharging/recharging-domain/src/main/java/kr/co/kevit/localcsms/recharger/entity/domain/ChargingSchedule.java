/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.recharger.entity.domain;

import java.io.Serializable;

/**
 * TB_RCDR001
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 10. 16.
 */
public class ChargingSchedule implements Serializable{

    /**  */
    private static final long serialVersionUID = 2711127996146322844L;
    
    /**
     * PK
     * SEQ INTEGER NOT NULL
     */
    private int seq;
    
    /**
     * 충전기 고유 ID
     * CS_UNIQ_ID VARCHAR(10) NOT NULL,
     */
    private String csUniqId;
    /**
     * ST_TM CHAR(14) NOT NULL
     */
    private String startTime;
    
    /**
     * ED_TM CHAR(14) NOT NULL
     */
    private String endTime;
    
    /**
     * 단위 초
     * DUR VARCHAR(3) NOT NULL
     */
    private String duration;
    
    /**
     * LIM_KW INTERGER NOT NULL
     */
    private int limitKW;

    /**
     * Get seq
     * @return seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * Set seq
     * @param seq
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * Get csUniqId
     * @return csUniqId
     */
    public String getCsUniqId() {
        return csUniqId;
    }

    /**
     * Set csUniqId
     * @param csUniqId
     */
    public void setCsUniqId(String csUniqId) {
        this.csUniqId = csUniqId;
    }

    /**
     * Get startTime
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Set startTime
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Get endTime
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Set endTime
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Get duration
     * @return duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Set duration
     * @param duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Get limitKW
     * @return limitKW
     */
    public int getLimitKW() {
        return limitKW;
    }

    /**
     * Set limitKW
     * @param limitKW
     */
    public void setLimitKW(int limitKW) {
        this.limitKW = limitKW;
    }

}
