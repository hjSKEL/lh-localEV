package kr.co.kevit.ocpp16.daemon.share;

import java.io.Serializable;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 30.
 */
public class JsonResultSet implements Serializable{

    /**  */
    private static final long serialVersionUID = -7983831275464328687L;

    private ResultStatus status;
    
    private ResultVo result;

    public JsonResultSet() {
        //
    }

    public JsonResultSet(ResultStatus status) {
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public ResultVo getResult() {
        return result;
    }

    public void setResult(ResultVo result) {
        this.result = result;
    }


}
