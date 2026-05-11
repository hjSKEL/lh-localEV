package kr.co.kevit.localcsms.common.util.exception;


import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 30.
 */
public enum KEVITErrorCode implements EnumInterface {
    
    SPEC_ERR("SPEC_ERR01", "전문 오류"),
    INTERNAL_ERR("INNER_ERR01", "내부 오류"),
    SMS_ERR("SMS_ERR01", "SMS"),
    PG_ERR("PG_ERR001", "PG_결재")
    ;
    
    
    private String code;
    
    private String desc;
    
    KEVITErrorCode(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        // 
        return code;
    }

    @Override
    public String getDesc() {
        // 
        return desc;
    }

    
}
