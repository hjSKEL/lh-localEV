package kr.co.kevit.localcsms.eai.sign.dto;

public class CSRRequest {
    private String csId;
    private String csr;
    private String certificateType;

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
}
