package kr.co.kevit.ocpp16.daemon.share;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 *
 */
public class ParamVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2564301582597690460L;

    private String param1;

    private String param2;

    private String param3;

    private String param4;

    private List<String> params;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

}
