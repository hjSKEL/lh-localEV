package kr.co.kevit.localcsms.ocpp.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp.model.OcppMessage;
import kr.co.kevit.ocpp16.enumtype.AcceptRejectStatus;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SignCertificateBean implements ControlerBean {

    private static final Logger log = LoggerFactory.getLogger(SignCertificateBean.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        kr.co.kevit.ocpp16.request.SignCertificate request = objectMapper.treeToValue(msg.getPayload(),
                kr.co.kevit.ocpp16.request.SignCertificate.class);
        log.info("SignCertificateBean cpCsId={} csr={}", cpCsId, request.getCsr());

        kr.co.kevit.ocpp16.response.SignCertificate response = new kr.co.kevit.ocpp16.response.SignCertificate();
        if (!StringUtils.isEmpty(request.getCsr())) {
            CloseableHttpResponse httpResp = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                Map<String, String> map = new HashMap<>();
                map.put("csId", cpCsId);
                map.put("certificateType", "ChargingStationCertificate");
                map.put("csr", request.getCsr());
                String txt = objectMapper.writeValueAsString(map);
                HttpPost post = new HttpPost("http://127.0.0.1:7000/ca/csr");
                post.setEntity(new StringEntity(txt, "UTF-8"));
                post.addHeader("Content-Type", "application/json");
                post.addHeader("Accept", "application/json");
                httpResp = httpClient.execute(post);
                String resultStr = EntityUtils.toString(httpResp.getEntity());
                log.info("[SignCertificate] csId={} response={}", cpCsId, resultStr);
                Map<?, ?> resultMap = objectMapper.readValue(resultStr, Map.class);
                if ("SUCCESS".equals(resultMap.get("result"))) {
                    response.setStatus(AcceptRejectStatus.Accepted);
                } else {
                    response.setStatus(AcceptRejectStatus.Rejected);
                }
            } catch (IOException e) {
                log.error("[SignCertificate] csId={} error={}", cpCsId, e.getMessage(), e);
                response.setStatus(AcceptRejectStatus.Rejected);
            } finally {
                if (httpResp != null) {
                    try {
                        httpResp.close();
                    } catch (Exception ex) {
                    }
                }
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (Exception ex) {
                    }
                }
            }
        } else {
            response.setStatus(AcceptRejectStatus.Rejected);
        }
        return objectMapper.valueToTree(response);
    }
}
