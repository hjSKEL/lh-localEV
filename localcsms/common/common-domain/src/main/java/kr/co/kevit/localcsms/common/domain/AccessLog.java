/*******************************************************************************
 * Copyright(c) 2019 AEA All rights reserved.
 * This software is the proprietary information of AEA.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 접근로그 (로그인/메뉴접근) TB_SYLG001
 * 
 * @author jhkim <a href="mailto:jhkim@kevit.co.kr">jhkim@kevit.co.kr</a>
 * @since 2019. 10. 18.
 */
public class AccessLog implements Serializable {

    /**  */
    private static final long serialVersionUID = 8638702393756288181L;
    /**
     * 시퀀스 SEQ NUMBER(20) NOT NULL,
     */
    private Long seq;
    private String logDate;
    private String logTime;
    private String logId;
    private String logIp;
    private String logUrl;
    private String logType;

    public AccessLog() {
    }

    public AccessLog(String userId, String logUrl, String logType, String logIp) {
        //
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("HHmmss");
        Date today = new Date();
        this.setLogDate(date.format(today));
        this.setLogTime(time.format(today));
        this.setLogId(userId);
        this.setLogIp(logIp);
        this.setLogUrl(logUrl);
        this.setLogType(logType);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogIp() {
        return logIp;
    }

    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}
