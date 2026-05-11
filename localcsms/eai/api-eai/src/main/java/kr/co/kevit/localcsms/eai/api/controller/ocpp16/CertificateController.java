package kr.co.kevit.localcsms.eai.api.controller.ocpp16;

import kr.co.kevit.localcsms.common.util.security.ByteUtils;
import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.type.CertificateUseType;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Certificate Management 그룹 엔드포인트.
 *
 * POST /getInstalledCertificateIds
 * POST /installCertificate
 * POST /deleteCertificate
 */
@RestController
@RequestMapping("/ocpp16")
public class CertificateController {

    private final Daemon16Client daemonClient;

    public CertificateController(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** Trigger a GetInstalledCertificateIds.req from the CSMS. */
    @PostMapping("/getInstalledCertificateIds")
    public ResponseEntity<ApiResult> getInstalledCertificateIds(
            @RequestParam String chargingStationIdentity,
            @RequestParam CertificateUseType certificateType) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("certificateType", certificateType.name());
        return ResponseEntity
                .ok(daemonClient.send(chargingStationIdentity, "GetInstalledCertificateIds", payload, null));
    }

    /** Trigger a InstallCertificate.req from the CSMS. */
    @PostMapping("/installCertificate")
    public ResponseEntity<ApiResult> installCertificate(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "InstallCertificate", payload, null));
    }

    /**
     * Trigger a DeleteCertificate.req from the CSMS.
     * Request body: CertificateHashData (hashAlgorithm, issuerNameHash,
     * issuerKeyHash, serialNumber)
     */
    @PostMapping("/deleteCertificate")
    public ResponseEntity<ApiResult> deleteCertificate(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {
        String certificate = (String) body.get("certificate");

        String serialNumber = "";
        String issuerKeyHash = "";
        String issuerNameHash = "";
        Map<String, Object> hashData = new HashMap<>();

        InputStream certIn = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            certIn = new ByteArrayInputStream(certificate.getBytes());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certIn);
            cert.checkValidity();

            SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(cert.getPublicKey().getEncoded());
            serialNumber = cert.getSerialNumber().toString(16);
            byte[] publicKeyBytes = subPubKeyInfo.getPublicKeyData().getBytes();
            issuerKeyHash = hashShaAlgorithm(publicKeyBytes, "SHA-256");
            issuerNameHash = hashShaAlgorithm(cert.getIssuerX500Principal().getEncoded(), "SHA-256");

            hashData.put("hashAlgorithm", "SHA256");
            hashData.put("issuerNameHash", issuerNameHash);
            hashData.put("issuerKeyHash", issuerKeyHash);
            hashData.put("serialNumber", serialNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.ok(ApiResult.rejected("deleteCertificate 인증서 오류"));
        } finally {
            if (certIn != null) {
                try {
                    certIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("certificateHashData", hashData);
        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "DeleteCertificate", payload, null));
    }

    private static String hashShaAlgorithm(byte[] value, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(value);
        byte[] issuerKeyHashByte = md.digest();
        return ByteUtils.byte2hexString(issuerKeyHashByte, issuerKeyHashByte.length);
    }
}
