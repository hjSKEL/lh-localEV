/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.ocpp20.bean.req;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.kevit.localcsms.ocpp20.bean.ControlerBean;
import kr.co.kevit.localcsms.ocpp20.model.OcppMessage;
import kr.co.kevit.localcsms.ocpp20.caller.ContractCertCaller;
import kr.co.kevit.localcsms.ocpp20.caller.vo.ContractCert;
import kr.co.kevit.localcsms.ocpp20.caller.vo.ContractCertReq;
import kr.co.kevit.localcsms.ocpp20.caller.vo.SignContractCert;
import kr.co.kevit.localcsms.certificate.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.certificate.process.CustomerCertService;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.util.string.StringUtils;
import kr.co.kevit.ocpp201.enumtype.Iso15118EVCertificateStatusEnumType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2021. 3. 28.
 */
@Component("Get15118EVCertificate")
public class Get15118EVCertificateBean implements ControlerBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(Get15118EVCertificateBean.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired(required = false)
    private CustomerCertService customerCertService;

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ObjectNode control(String cpCsId, OcppMessage msg) throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //
        String text = msg.getPayload().toString();
        kr.co.kevit.ocpp201.request.Get15118EVCertificate obj = objectMapper.readValue(text,kr.co.kevit.ocpp201.request.Get15118EVCertificate.class);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get15118EVCertificateBean.control : {}", text);
        }
        kr.co.kevit.ocpp201.response.Get15118EVCertificate response = new kr.co.kevit.ocpp201.response.Get15118EVCertificate();
        if(StringUtils.isEmpty(obj.getExiRequest()) || StringUtils.isEmpty(obj.getIso15118SchemaVersion())) {
            response.setStatus(Iso15118EVCertificateStatusEnumType.Failed);
            return objectMapper.valueToTree(response);
        }
        
        switch(obj.getAction()) { 
        case Install:
            /* ISO15118 CertificateInstallationReq
         - id
           - OEM ProvisioningCert
           - List Of RootCertificateIDs 
             */
        case Update:
            /* ISO15118 CertificateUpdateReq
        - id
          - ContractSignatureCertChain
          - eMAID
          - ListOfRootCertificateIDs
             */

             /*
            ContractCertReq req = new ContractCertReq();
            req.setCertificateInstallationReq(obj.getExiRequest());
            req.setXsdMsgDefNamespace(obj.getIso15118SchemaVersion());
            SignContractCert res = ContractCertCaller.getSignedContractData(req);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(objectMapper.writeValueAsString(res));
            }
            ContractCert contractCert = res.getEmaidContent().get(0);;
            String eMaid = contractCert.getEmaid();
            CustomerCert cert = customerCertService.retrieveCustomerCertByEmaid(eMaid);
            cert.setStatus("CERT02");
            cert.setWriter(new Writer("E00000001"));
            response.setExiResponse(contractCert.getCertificateInstallationRes());
            customerCertService.modifyCustomerCert(cert);
            */
            String testExiResponse = "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";
            response.setExiResponse(testExiResponse);
            break;
        default :
            response.setStatus(Iso15118EVCertificateStatusEnumType.Failed);    
            return objectMapper.valueToTree(response);
        }
        // ContractSignatureCertChain , ContractSignatureEncryptedPrivateKey, DHpublickey, ContractID
        response.setStatus(Iso15118EVCertificateStatusEnumType.Accepted);
        return objectMapper.valueToTree(response);
    }
}