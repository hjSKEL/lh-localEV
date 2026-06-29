/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.ocpp201.exception;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 3. 11.
 */
public class OCPPException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = -8754311248198239355L;

    private OCPPErrorCode code;

    private String msg;

    public OCPPException(OCPPErrorCode code) {
        //
        this.code = code;
    }

    public OCPPException(OCPPErrorCode code, String msg) {
        //
        this.code = code;
        this.msg = msg;
    }

    public OCPPErrorCode getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
