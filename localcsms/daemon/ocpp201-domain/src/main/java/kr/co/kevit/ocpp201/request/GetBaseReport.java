/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.ocpp201.request;

import java.util.Map;

import kr.co.kevit.ocpp201.enumtype.ReportBaseEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 1. 14.
 */
public class GetBaseReport {
    
    /**
     * required
     */
    private Integer requestId;
    
    /**
     * required
     */
    private ReportBaseEnumType reportBase;

    private Map<String, Object> customData;

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public ReportBaseEnumType getReportBase() {
        return reportBase;
    }

    public void setReportBase(ReportBaseEnumType reportBase) {
        this.reportBase = reportBase;
    }

}
