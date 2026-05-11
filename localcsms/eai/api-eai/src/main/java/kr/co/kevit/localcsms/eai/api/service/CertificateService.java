package kr.co.kevit.localcsms.eai.api.service;

import kr.co.kevit.localcsms.eai.api.client.Daemon16Client;
import kr.co.kevit.localcsms.eai.api.dto.ApiResult;
import kr.co.kevit.localcsms.eai.api.dto.model.CertificateHashData;
import kr.co.kevit.localcsms.eai.api.dto.type.CertificateUseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Certificate Management 관련 OCPP 1.6 액션 서비스.
 *
 * getInstalledCertificateIds / installCertificate / deleteCertificate
 */
@Service
public class CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateService.class);

    private final Daemon16Client daemonClient;

    public CertificateService(Daemon16Client daemonClient) {
        this.daemonClient = daemonClient;
    }

    /** GetInstalledCertificateIds.req 전송 */
    public ApiResult getInstalledCertificateIds(String csId, CertificateUseType certificateType) {
        log.info("[API] getInstalledCertificateIds csId={} type={}", csId, certificateType);
        return daemonClient.send(csId, "GetInstalledCertificateIds",
                Map.of("certificateType", certificateType.name()), null);
    }

    /** InstallCertificate.req 전송 */
    public ApiResult installCertificate(String csId, CertificateUseType certificateType,
                                        String certificate) {
        log.info("[API] installCertificate csId={} type={}", csId, certificateType);
        return daemonClient.send(csId, "InstallCertificate",
                Map.of("certificateType", certificateType.name(), "certificate", certificate), null);
    }

    /** DeleteCertificate.req 전송 */
    public ApiResult deleteCertificate(String csId, CertificateHashData certificateHashData) {
        log.info("[API] deleteCertificate csId={} serial={}", csId,
                certificateHashData == null ? null : certificateHashData.getSerialNumber());
        return daemonClient.send(csId, "DeleteCertificate", certificateHashData, null);
    }
}
