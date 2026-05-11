/*******************************************************************************
 * Copyright(c) 2023 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.share;

import java.io.Serializable;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2023. 6. 9.
 */
public class JsonResultSet implements Serializable{

    /**  */
    private static final long serialVersionUID = -7983831275464328687L;

    private ResultStatus status;
    
    private Object result;

    public JsonResultSet(ResultStatus status) {
        this.status = status;
    }
    
    public JsonResultSet(ResultStatus status, Object result) {
        this.status = status;
        this.result = result;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
