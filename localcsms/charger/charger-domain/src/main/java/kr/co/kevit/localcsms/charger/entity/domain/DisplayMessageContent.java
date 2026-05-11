package kr.co.kevit.localcsms.charger.entity.domain;

import java.io.Serializable;

/**
 * TB_CHDM002
 * OCPP 2.x MessageContent - 디스플레이 메시지 내용
 */
public class DisplayMessageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /** PK: 메시지 ID  MSG_ID INT */
    private int messageId;

    /** PK: 언어 코드 (ko / en …)  MSG_LANG CHAR(2) */
    private String msgLanguage;

    /** 메시지 포맷 공통코드 DMFM00 (ASCII/HTML/URI/UTF8/QRCODE)  MSG_FRMT CHAR(6) */
    private String msgFormat;

    /** 메시지 내용  MSG_CONT VARCHAR(512) */
    private String msgContent;

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public String getMsgLanguage() { return msgLanguage; }
    public void setMsgLanguage(String msgLanguage) { this.msgLanguage = msgLanguage; }

    public String getMsgFormat() { return msgFormat; }
    public void setMsgFormat(String msgFormat) { this.msgFormat = msgFormat; }

    public String getMsgContent() { return msgContent; }
    public void setMsgContent(String msgContent) { this.msgContent = msgContent; }
}
