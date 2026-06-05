package kr.co.kevit.localcsms.eai.api.controller.ocpp2x;

import kr.co.kevit.localcsms.common.util.security.ByteUtils;
import kr.co.kevit.localcsms.eai.api.client.Daemon2xClient;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
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
import java.util.List;
import java.util.Map;

/**
 * OCPP 2.0.1 Certificate 그룹 엔드포인트.
 *
 * POST /ocpp2x/installCertificate?chargingStationIdentity= (body: {
 * certificateType, certificate })
 * POST /ocpp2x/deleteCertificate?chargingStationIdentity= (body: {
 * certificateHashData: { ... } })
 * POST /ocpp2x/getInstalledCertificateIds?chargingStationIdentity= (body: {
 * certificateType: [...] })
 */
@RestController
@RequestMapping("/ocpp2x")
public class Ocpp2xCertificateController {

    private final Daemon2xClient daemonClient;

    public Ocpp2xCertificateController(Daemon2xClient daemonClient) {
        this.daemonClient = daemonClient;
    }

    /**
     * body: { "certificateType": "...", "certificate": "PEM 문자열" }
     */
    @PostMapping("/installCertificate")
    public ResponseEntity<ApiResult> installCertificate(
            @RequestParam String chargingStationIdentity,
            @RequestBody Map<String, Object> body) {

        return ResponseEntity.ok(daemonClient.send(chargingStationIdentity, "InstallCertificate", body, null));
    }

    /**
     * body: { "certificate": "PEM 문자열", "issuerCertificate": "PEM 문자열" }
     * certificate 에서 certificateHashData
     * (hashAlgorithm/issuerNameHash/issuerKeyHash/serialNumber) 를 자동 계산하여 전송한다.
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

    /**
     * certificateType (optional, 복수):
     * &certificateType=V2GRootCertificate&certificateType=MORootCertificate
     */
    @RequestMapping(value = "/getInstalledCertificateIds", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<ApiResult> getInstalledCertificateIds(
            @RequestParam String chargingStationIdentity,
            @RequestParam(required = false) List<String> certificateType) {

        Map<String, Object> payload = new HashMap<>();
        if (certificateType != null && !certificateType.isEmpty()) {
            payload.put("certificateType", certificateType);
        }

        return ResponseEntity
                .ok(daemonClient.send(chargingStationIdentity, "GetInstalledCertificateIds", payload, null));
    }
}
