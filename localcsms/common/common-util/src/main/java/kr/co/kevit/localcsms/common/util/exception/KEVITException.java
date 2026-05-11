package kr.co.kevit.localcsms.common.util.exception;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 15.
 */
public class KEVITException extends RuntimeException{

    /**  */
    private static final long serialVersionUID = 5123110510349968124L;

    private String code;
    
    public KEVITException(String code){
        //
        super(code);
        this.code = code;
        
    }
    
    public KEVITException(String code, String msg){
        //
        super(code + "\n" + msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
