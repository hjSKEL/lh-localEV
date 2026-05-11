/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.share;

import java.io.Serializable;

/**
 * @author wj.lee <a href="mailto:wj.lee@kevit.co.kr">wj.lee@kevit.co.kr</a>
 * @since 2023. 01. 16.
 */
public class CmdVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3521811993881858740L;

    private String ip;

    private String port;

    private String msg;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
