package kr.co.kevit.localcsms.eai.api.dto.model;

import kr.co.kevit.localcsms.eai.api.dto.type.HashAlgorithmType;

import java.time.OffsetDateTime;

/** OCPP 1.6 Security Firmware (SignedUpdateFirmware) */
public class Firmware {

    /** 펌웨어 다운로드 URL */
    private String location;
    /** 다운로드 시작 시각 */
    private OffsetDateTime retrieveDateTime;
    /** 설치 시작 시각 (옵션) */
    private OffsetDateTime installDateTime;
    /** 서명 인증서 (PEM, 옵션) */
    private String signingCertificate;
    /** 펌웨어 파일 서명 (base64, 옵션) */
    private String signature;

    public String getLocation()                         { return location; }
    public void setLocation(String v)                   { this.location = v; }

    public OffsetDateTime getRetrieveDateTime()         { return retrieveDateTime; }
    public void setRetrieveDateTime(OffsetDateTime v)   { this.retrieveDateTime = v; }

    public OffsetDateTime getInstallDateTime()          { return installDateTime; }
    public void setInstallDateTime(OffsetDateTime v)    { this.installDateTime = v; }

    public String getSigningCertificate()               { return signingCertificate; }
    public void setSigningCertificate(String v)         { this.signingCertificate = v; }

    public String getSignature()                        { return signature; }
    public void setSignature(String v)                  { this.signature = v; }
}
