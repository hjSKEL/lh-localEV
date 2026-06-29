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
public enum OCPPErrorCode {
    FormatViolation("", "Payload for Action is syntactically incorrect"),
    GenericError("", "Any other error not covered by the more specific error codes in this table"),
    InternalError("", "An internal error occurred and the receiver was not able to process the requested Action successfully"),
    MessageTypeNotSupported("", "A message with an Message Type Number received that is not supported by this implementation."),
    NotImplemented("", "Requested Action is not known by receiver"),
    NotSupported("", "Requested Action is recognized but not supported by the receiver"),
    OccurrenceConstraintViolation("", "Payload for Action is syntactically correct but at least one of the fields violates occurrence constraints"),
    PropertyConstraintViolation("", "Payload is syntactically correct but at least one field contains an invalid value"),
    ProtocolError("", "Payload for Action is not conform the PDU structure"),
    RpcFrameworkError("", "Content of the call is not a valid RPC Request, for example: MessageId could not be read."),
    SecurityError("", "During the processing of Action a security issue occurred preventing receiver from completing the Action successfully"),
    TypeConstraintViolation("", "Payload for Action is syntactically correct but at least one of the fields violates data type constraints");

    private String code;

    private String desc;

    OCPPErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OCPPErrorCode getOCPPErrorCodeByteCode(String code) {
        for (OCPPErrorCode err : OCPPErrorCode.values()) {
            if (err.getCode().equals(code)) {
                return err;
            }
        }
        return null;
    }
}
