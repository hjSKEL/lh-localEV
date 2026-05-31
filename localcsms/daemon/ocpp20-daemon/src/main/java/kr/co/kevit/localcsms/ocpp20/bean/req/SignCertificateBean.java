/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation.
 * All rights reserved. This software is the proprietary information of
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.common.util.string.StringConstants;
import kr.co.kevit.ocpp201.enumtype.CertificateSigningUseEnumType;
import kr.co.kevit.ocpp201.enumtype.GenericStatusEnumType;

/**
 *
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2021. 8. 17.
 */
@Component("SignCertificate")
public class SignCertificateBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignCertificateBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String[] csIds = cpCsId.split(StringConstants.DASH);

        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.SignCertificate obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.SignCertificate.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SignCertificateBean text : {}", text);
        }
        kr.co.kevit.ocpp201.response.SignCertificate respone = new kr.co.kevit.ocpp201.response.SignCertificate();
        if(StringUtils.isEmpty(obj.getCsr())) {
            respone.setStatus(GenericStatusEnumType.Rejected);
            return objectMapper.valueToTree(respone);
        }

        respone.setStatus(GenericStatusEnumType.Accepted);
        if(obj.getCertificateType() == CertificateSigningUseEnumType.ChargingStationCertificate) {
            CloseableHttpResponse httpResp = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                Map<String, String> map = new HashMap<>();
                map.put("csId", cpCsId);
                map.put("csr", obj.getCsr());
                String txt = objectMapper.writeValueAsString(map);
                HttpPost post = new HttpPost("http://127.0.0.1:7000/ca/csr");
                post.setEntity(new StringEntity(txt, "UTF-8"));
                post.addHeader("Content-Type", "application/json");
                post.addHeader("Accept", "application/json");
                httpResp = httpClient.execute(post);
                String resultStr = EntityUtils.toString(httpResp.getEntity());
                LOGGER.info("[SignCertificate] csId={} response={}", cpCsId, resultStr);
                Map<?, ?> resultMap = objectMapper.readValue(resultStr, Map.class);
                if ("SUCCESS".equals(resultMap.get("result"))) {
                    respone.setStatus(GenericStatusEnumType.Accepted);
                } else {
                    respone.setStatus(GenericStatusEnumType.Rejected);
                }
            } catch (IOException e) {
                LOGGER.error("[SignCertificate] csId={} error={}", cpCsId, e.getMessage(), e);
                respone.setStatus(GenericStatusEnumType.Rejected);
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
        } else if(obj.getCertificateType() == CertificateSigningUseEnumType.V2GCertificate) {
            CloseableHttpResponse httpResp = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            try {
                Map<String, String> map = new HashMap<>();
                map.put("csId", cpCsId);
                map.put("csr", obj.getCsr());
                String txt = objectMapper.writeValueAsString(map);
                HttpPost post = new HttpPost("http://127.0.0.1:7001/ca/csr");
                post.setEntity(new StringEntity(txt, "UTF-8"));
                post.addHeader("Content-Type", "application/json");
                post.addHeader("Accept", "application/json");
                httpResp = httpClient.execute(post);
                String resultStr = EntityUtils.toString(httpResp.getEntity());
                LOGGER.info("[SignCertificate] csId={} response={}", cpCsId, resultStr);
                Map<?, ?> resultMap = objectMapper.readValue(resultStr, Map.class);
                if ("SUCCESS".equals(resultMap.get("result"))) {
                    respone.setStatus(GenericStatusEnumType.Accepted);
                } else {
                    respone.setStatus(GenericStatusEnumType.Rejected);
                }
            } catch (IOException e) {
                LOGGER.error("[SignCertificate] csId={} error={}", cpCsId, e.getMessage(), e);
                respone.setStatus(GenericStatusEnumType.Rejected);
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
        }

        return objectMapper.valueToTree(respone);
    }
}
