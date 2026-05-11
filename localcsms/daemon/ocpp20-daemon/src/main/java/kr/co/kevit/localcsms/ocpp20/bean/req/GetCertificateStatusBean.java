/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.io.IOException;

import org.bouncycastle.cert.ocsp.OCSPResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.ocpp20.caller.OcspCaller;
import kr.co.kevit.localcsms.common.util.security.Base64;
import kr.co.kevit.ocpp201.domain.OCSPRequestDataType;
import kr.co.kevit.ocpp201.enumtype.GetCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 3. 19.
 */
@Component("GetCertificateStatus")
public class GetCertificateStatusBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCertificateStatusBean.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.GetCertificateStatus request = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.GetCertificateStatus.class);
        kr.co.kevit.ocpp201.response.GetCertificateStatus response = new kr.co.kevit.ocpp201.response.GetCertificateStatus();
        
        OCSPRequestDataType ocspReqData = request.getOcspRequestData();
        OCSPResp ocspResp = OcspCaller.varifyCert(ocspReqData);
        if(ocspResp == null) {
            response.setStatus(GetCertificateStatusEnumType.Failed);
            return objectMapper.valueToTree(response);
        }else {
            try {
                response.setOcspResult(Base64.toString(ocspResp.getEncoded()));
            } catch (IOException e) {
                // 
                LOGGER.error(e.getMessage(), e);
            }
        }
        
        switch(ocspResp.getStatus()) {
        case 0://SUCCESSFUL
            response.setStatus(GetCertificateStatusEnumType.Accepted);
            break;
        case 1://MALFORMED_REQUEST
        case 2://INTERNAL_ERROR
        case 3://TRY_LATER
        case 5://SIG_REQUIRED
        case 6://UNAUTHORIZED
        default:
            response.setStatus(GetCertificateStatusEnumType.Failed);
        }
        return objectMapper.valueToTree(response);
    }
}
