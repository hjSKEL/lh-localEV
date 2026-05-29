package kr.co.kevit.localcsms.smartcharging.entity.domain;

import kr.co.kevit.localcsms.common.domain.Writer;

import java.io.Serializable;

/**
 * TB_CSCF001 — CS 별 OCPP 변수 캐시(CSMS 측 기록).
 *
 * <p>현재는 {@code MaxExternalConstraintsId}(SmartChargingCtrlr) 만 추적.
 * 향후 다른 CS-config 변수 확장 가능.</p>
 */
public class CsConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpId;
    private String csId;
    private Integer maxExtConstraintsId;
    private Writer writer;

    public String getCpId() { return cpId; }
    public void setCpId(String cpId) { this.cpId = cpId; }

    public String getCsId() { return csId; }
    public void setCsId(String csId) { this.csId = csId; }

    public Integer getMaxExtConstraintsId() { return maxExtConstraintsId; }
    public void setMaxExtConstraintsId(Integer maxExtConstraintsId) { this.maxExtConstraintsId = maxExtConstraintsId; }

    public Writer getWriter() { return writer; }
    public void setWriter(Writer writer) { this.writer = writer; }
}
