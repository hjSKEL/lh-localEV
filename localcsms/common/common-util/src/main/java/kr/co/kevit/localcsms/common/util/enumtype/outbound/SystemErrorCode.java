package kr.co.kevit.localcsms.common.util.enumtype.outbound;

import java.util.ArrayList;
import java.util.List;

import kr.co.kevit.localcsms.common.util.enumtype.EnumInterface;
import kr.co.kevit.localcsms.common.util.enumtype.EnumKeyValue;

/**
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a>
 *
 */
public enum SystemErrorCode implements EnumInterface{
	//
	NETWORK("SEC01", "통신오류"),
	NULL("SEC02", "NULL")
	;
	
	SystemErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	private String code;
	private String desc;
	
	@Override
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
    public static SystemErrorCode getTypeByCode(String code) {
    	SystemErrorCode[] values = values();
        for (SystemErrorCode value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
	
	public static List<EnumKeyValue> getKeyValues() {
		List<EnumKeyValue> nameValues = new ArrayList<>();
		
		SystemErrorCode[] values = SystemErrorCode.values();
		for (SystemErrorCode value : values) {
			nameValues.add(new EnumKeyValue(value.name(), value.getDesc()));
		}
		
		return nameValues;
	}
}