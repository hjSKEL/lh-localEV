package kr.co.kevit.localcsms.eai.vo;

import java.io.Serializable;

/**
 * 
 * @author wj.lee <a href="mailto:wj.lee@nextree.co.kr">wj.lee@nextree.co.kr</a>
 *
 */
public class ResultVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 1352441957883020905L;

    private String code;

    private String message;

    public ResultVo() {
        this.code = "";
        this.message = "";;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
