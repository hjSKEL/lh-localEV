package kr.co.kevit.localcsms.eai.api.dto.model;

import kr.co.kevit.localcsms.eai.api.dto.type.HashAlgorithmType;

/** OCPP 1.6 CertificateHashData (DeleteCertificate) */
public class CertificateHashData {

    private HashAlgorithmType hashAlgorithm;
    /** 발급자 이름의 해시 (hex) */
    private String issuerNameHash;
    /** 발급자 공개키의 해시 (hex) */
    private String issuerKeyHash;
    /** 인증서 시리얼 번호 (hex) */
    private String serialNumber;

    public HashAlgorithmType getHashAlgorithm()         { return hashAlgorithm; }
    public void setHashAlgorithm(HashAlgorithmType v)   { this.hashAlgorithm = v; }

    public String getIssuerNameHash()                   { return issuerNameHash; }
    public void setIssuerNameHash(String v)             { this.issuerNameHash = v; }

    public String getIssuerKeyHash()                    { return issuerKeyHash; }
    public void setIssuerKeyHash(String v)              { this.issuerKeyHash = v; }

    public String getSerialNumber()                     { return serialNumber; }
    public void setSerialNumber(String v)               { this.serialNumber = v; }
}
