package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.charger.entity.domain.CsFirmware;
import kr.co.kevit.localcsms.charger.process.CsFirmwareService;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignedFirmwareStatusNotificationBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(SignedFirmwareStatusNotificationBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CsFirmwareService csFirmwareService;

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        kr.co.kevit.ocpp16.request.SignedFirmwareStatusNotification request =
                objectMapper.treeToValue(msg.getPayload(), kr.co.kevit.ocpp16.request.SignedFirmwareStatusNotification.class);
        log.debug("SignedFirmwareStatusNotificationBean cpCsId={} requestId={} status={}",
                cpCsId, request.getRequestId(), request.getStatus());

        String csfw = toFirmwareStatusCode(request.getStatus() != null ? request.getStatus().name() : null);
        if (csfw != null) {
            updateFirmwareStatus(cpCsId, csfw);
        }

        return objectMapper.createObjectNode();
    }

    private void updateFirmwareStatus(String cpCsId, String statusCode) {
        try {
            int idx = cpCsId.lastIndexOf('-');
            if (idx < 0) return;
            String cpId = cpCsId.substring(0, idx);
            String csId = cpCsId.substring(idx + 1);

            CsFirmware fw = new CsFirmware();
            fw.setCpId(cpId);
            fw.setCsId(csId);
            fw.setStatus(statusCode);
            fw.setRequestEmployeeId(kr.co.kevit.localcsms.common.util.string.StringConstants.SYSTEM_EMPLOYEE);
            fw.setUpdateDate(new java.util.Date());
            csFirmwareService.modifyFirmwareStatus(fw);
        } catch (Exception e) {
            log.warn("CsFirmware 상태 업데이트 실패: {}", e.getMessage(), e);
        }
    }

    /** OCPP 1.6 Security SignedFirmwareStatus → CSFW 코드 매핑 */
    private String toFirmwareStatusCode(String ocppStatus) {
        if (ocppStatus == null) return null;
        switch (ocppStatus) {
            case "Downloading":             return "CSFW02";
            case "Downloaded":              return "CSFW03";
            case "DownloadFailed":          return "CSFW04";
            case "Installing":              return "CSFW05";
            case "Installed":               return "CSFW06";
            case "InstallationFailed":      return "CSFW07";
            case "InstallVerificationFailed": return "CSFW07";
            case "InvalidSignature":        return "CSFW04";
            default:                        return null; // Idle, DownloadScheduled 등
        }
    }
}
