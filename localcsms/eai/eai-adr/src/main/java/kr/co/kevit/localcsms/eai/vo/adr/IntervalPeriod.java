/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 12.
 */
public class IntervalPeriod {
    
    /**
     * 
    : The start time of an interval or set of intervals, e.g. "2023-06-15T12:58:08.000Z".
     */
    private String start;
    
    /**
     * 
    : The duration of an interval or set of intervals, e.g. "PT1H".
     */
    private String duration;
    
    /**
     * 
    : Indicates a randomization time that may be applied to start, e.g. "PT5M".
     */
    private String randomizeStart;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRandomizeStart() {
        return randomizeStart;
    }

    public void setRandomizeStart(String randomizeStart) {
        this.randomizeStart = randomizeStart;
    }
}
