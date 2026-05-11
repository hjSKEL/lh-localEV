/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.caller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.CertID;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.localcsms.common.util.security.ByteUtils;
import kr.co.kevit.localcsms.common.util.security.CertStringUtils;
import kr.co.kevit.ocpp201.domain.OCSPRequestDataType;
import kr.co.kevit.ocpp201.enumtype.HashAlgorithmEnumType;
import kr.co.kevit.ocpp201.enumtype.IdTokenEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2024. 3. 15.
 */
public class OcspCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger(OcspCaller.class);

    public static boolean validateCert(kr.co.kevit.ocpp201.request.Authorize request) {
        //
        if (request.getIdToken().getType() != IdTokenEnumType.eMAID) {
            return false;
        }

        String certChain = request.getCertificate();
        List<OCSPRequestDataType> ocspRequestData = request.getIso15118CertificateHashData();

        if (StringUtils.isEmpty(certChain) && (ocspRequestData == null || ocspRequestData.isEmpty())) {
            return false;
        }
        OCSPResp ocspResp = null;
        try {
            if (!StringUtils.isEmpty(certChain)) {
                ocspResp = varifyCert(certChain);
            } else {
                ocspResp = varifyCert(ocspRequestData);
            }
            return ocspResp.getStatus() == 1;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return true;
    }
    
    public static OCSPResp varifyCert(OCSPRequestDataType data){
        //
        try {
            byte[] issuerKeyHash = ByteUtils.hexStringToByteArray(data.getIssuerKeyHash());
            byte[] issuerNameHash = ByteUtils.hexStringToByteArray(data.getIssuerNameHash());
            ASN1OctetString issuerKeyHash1 = new DEROctetString(issuerKeyHash);
            ASN1OctetString issuerNameHash1 = new DEROctetString(issuerNameHash);

            // SHA - 1 1.3.14.3.2.26
            // SHA-256 2.16.840.1.101.3.4.2.1
            CertID certId = new CertID(algorith2ASN1OID(data.getHashAlgorithm()), issuerNameHash1, issuerKeyHash1,
                    new ASN1Integer(new BigInteger(data.getSerialNumber(), 16)));
            //
            CertificateID id = new CertificateID(certId);
            OCSPReqBuilder ocspGen = new OCSPReqBuilder();
            ocspGen.addRequest(id);
            OCSPReq request = ocspGen.build();
            byte[] reqByte = request.getEncoded();
            LOGGER.info(ByteUtils.byte2hexString(reqByte, reqByte.length));
            //return sendPost(data.getResponderURL(), reqByte);
            return sendPost("http://localhost:3012", reqByte);//OCPP2.0.1  TC_M_24_CSMS Test
        }catch(Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    private static OCSPResp varifyCert(List<OCSPRequestDataType> ocspRequestData) throws IOException, OCSPException {
        //
        OCSPResp ocspResp = null;
        for (OCSPRequestDataType data : ocspRequestData) {
            byte[] issuerKeyHash = ByteUtils.hexStringToByteArray(data.getIssuerKeyHash());
            byte[] issuerNameHash = ByteUtils.hexStringToByteArray(data.getIssuerNameHash());
            ASN1OctetString issuerKeyHash1 = new DEROctetString(issuerKeyHash);
            ASN1OctetString issuerNameHash1 = new DEROctetString(issuerNameHash);

            // SHA - 1 1.3.14.3.2.26
            // SHA-256 2.16.840.1.101.3.4.2.1
            CertID certId = new CertID(algorith2ASN1OID(data.getHashAlgorithm()), issuerNameHash1, issuerKeyHash1,
                    new ASN1Integer(new BigInteger(data.getSerialNumber(), 16)));
            //
            CertificateID id = new CertificateID(certId);
            OCSPReqBuilder ocspGen = new OCSPReqBuilder();
            ocspGen.addRequest(id);
            OCSPReq request = ocspGen.build();
            byte[] reqByte = request.getEncoded();
            LOGGER.info(ByteUtils.byte2hexString(reqByte, reqByte.length));
            ocspResp = sendPost(data.getResponderURL(), reqByte);
            boolean isTrue = validateOCSPResponse(ocspResp);
            if (isTrue) {
                return ocspResp;
            }
        }
        return ocspResp;
    }

    private static boolean validateOCSPResponse(OCSPResp response) throws OCSPException {
        BasicOCSPResp basicResponse = (BasicOCSPResp) response.getResponseObject();

        // Verify response
        if (basicResponse != null) {
            SingleResp[] responses = basicResponse.getResponses();
            for (SingleResp singleResponse : responses) {
                CertificateStatus status = singleResponse.getCertStatus();
                if (status == CertificateStatus.GOOD) {
                    LOGGER.info("Certificate is valid.");
                    return true;
                } else if (status instanceof RevokedStatus) {
                    LOGGER.info("Certificate is revoked.");
                } else {
                    LOGGER.info("Certificate status unknown.");
                }
            }
        } else {
            LOGGER.error("Error: Response is null.");
        }
        return false;
    }

    private static AlgorithmIdentifier algorith2ASN1OID(HashAlgorithmEnumType algorith) {
        switch (algorith) {
        case SHA256:
            return new AlgorithmIdentifier(new ASN1ObjectIdentifier("2.16.840.1.101.3.4.2.1"), null);
        case SHA384:
            return new AlgorithmIdentifier(new ASN1ObjectIdentifier("2.16.840.1.101.3.4.2.2"), null);
        case SHA512:
            return new AlgorithmIdentifier(new ASN1ObjectIdentifier("2.16.840.1.101.3.4.2.3"), null);
        default:
            // SHA-1
            return new AlgorithmIdentifier(new ASN1ObjectIdentifier("1 1.3.14.3.2.26"), null);
        }
    }

    private static OCSPResp varifyCert(String certChain) throws CertificateException, NoSuchProviderException, IOException, OperatorCreationException, OCSPException {
        //
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        CertificateFactory certFactory = CertificateFactory.getInstance("X509", "BC");
        List<String> certs = CertStringUtils.parseChain(certChain);
        if(certs.size() != 2) {
            return null;
        }
        String evCert = certs.get(0);
        LOGGER.debug("EV CERT : {}", evCert);
        X509Certificate csCert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(evCert.getBytes()));
        BigInteger serial = new BigInteger(csCert.getSerialNumber().toString(16), 16);
        LOGGER.info("Serial number: {}", serial);
        String responderURL = getResponderURL(csCert);
        //
        String subCaCert = certs.get(1);
        LOGGER.debug("SUB CA CERT : {}", subCaCert);
        X509Certificate subCaCrt = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(subCaCert.getBytes()));
        LOGGER.info("ROOT CA subject: {}", subCaCrt.getIssuerX500Principal().getName("CANONICAL"));
        
        JcaDigestCalculatorProviderBuilder digestCalculatorProviderBuilder = new JcaDigestCalculatorProviderBuilder();
        DigestCalculatorProvider digestCalculatorProvider = digestCalculatorProviderBuilder.build();
        DigestCalculator digestCalculator = digestCalculatorProvider.get(CertificateID.HASH_SHA1);

        
        // Generate the id for the certificate we are looking for
        CertificateID id = new CertificateID(digestCalculator, new JcaX509CertificateHolder(subCaCrt), serial);
        OCSPReqBuilder ocspGen = new OCSPReqBuilder();
        ocspGen.addRequest(id);

        OCSPReq request = ocspGen.build();
        byte[] reqByte = request.getEncoded();
        LOGGER.info(ByteUtils.byte2hexString(reqByte, reqByte.length));
        OCSPResp ocspResp = sendPost(responderURL, reqByte);
        validateOCSPResponse(ocspResp);;
        return ocspResp;
    }
    
    private static String getResponderURL(X509Certificate cert) {
        try {
            // 확장 필드 중에서 "Authority Information Access" 확장 필드 찾기
            byte[] aiaExtensionValue = cert.getExtensionValue("1.3.6.1.5.5.7.1.1");
            LOGGER.info("aiaExtensionValue HEX : {} ", ByteUtils.toHexFromByte(aiaExtensionValue));
            if (aiaExtensionValue != null) {
                // 확장 필드 값 파싱
                ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(aiaExtensionValue));
                ASN1Primitive aiaObject = asn1InputStream.readObject();
                asn1InputStream.close();

                // 확장 필드 값이 시퀀스인지 확인
                if (aiaObject instanceof ASN1Sequence) {
                    ASN1Sequence aiaSequence = (ASN1Sequence) aiaObject;

                    // 확장 필드 값에서 OCSP 응답기 URL 찾기
                    for (int i = 0; i < aiaSequence.size(); i++) {
                        ASN1Sequence aiaEntry = (ASN1Sequence) aiaSequence.getObjectAt(i);
                        if (aiaEntry.size() == 2) {
                            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) aiaEntry.getObjectAt(0);
                            if (oid.equals(new ASN1ObjectIdentifier("1.3.6.1.5.5.7.48.1"))) { // OCSP OID
                                // OCSP 응답기 URL 추출
                                byte[] urlBytes = ((ASN1OctetString) aiaEntry.getObjectAt(1)).getOctets();
                                String ocspResponderURL = new String(urlBytes);

                                LOGGER.info("OCSP Responder URL: {}", ocspResponderURL);
                                return ocspResponderURL;
                            }else {
                                LOGGER.error("Not oid.equals(new ASN1ObjectIdentifier(1.3.6.1.5.5.7.48.1))");
                            }
                        }else {
                            LOGGER.error("aiaEntry.size() != 2, {}", aiaEntry.size());
                        }
                    }
                }else {
                    String aiaExtensionValueStr = new String(aiaExtensionValue);
                    String ocspResponderURL = aiaExtensionValueStr.substring(aiaExtensionValueStr.indexOf("http"));
                    LOGGER.info("OCSP Responder URL: {}", ocspResponderURL);
                    return ocspResponderURL;
                }
            }else {
                LOGGER.error("aiaExtensionValue is NULL");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private static OCSPResp sendPost(String urlPath, byte[] request) throws IOException {

        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/ocsp-request");
            connection.setRequestProperty("Accept", "application/ocsp-response");
            connection.setRequestProperty("Content-Length", "4096");

            try (OutputStream output = connection.getOutputStream()) {
                output.write(request);
            }

            try (InputStream input = connection.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    baos.write(buffer, 0, bytesRead);
                }
                LOGGER.info("Http response code: {0}", connection.getResponseCode());
                byte[] respBytes = baos.toByteArray();
                return new OCSPResp(respBytes);
            }
        } finally {
            connection.disconnect();
        }
    }

}
