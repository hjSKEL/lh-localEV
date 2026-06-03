/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.customer.process;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.kevit.localcsms.customer.entity.domain.CustomerCert;
import kr.co.kevit.localcsms.common.domain.Writer;
import kr.co.kevit.localcsms.common.testcase.AbstractTestCase;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2024. 4. 3.
 */
public class CustomerCertServiceTest extends AbstractTestCase{
    
    @Autowired
    private CustomerCertService service; 

    private CustomerCert registerCustomerCert() {
        CustomerCert cert = new CustomerCert();
        cert.setCertValidTo("2025-12-31T23:59:59+09:00");
        cert.setCertValidFrom("2024-01-01T01:01:01+09:00");
        cert.setCustomerId("C00000001");
        cert.seteMaid("KRKEVTOPEN00001");
        cert.setPcid("HUBOPENPROVCERT001");
        cert.setXsdMsgDefNamespace("urn:iso:15118:2:2013:MsgDef");
        cert.setStatus("CERT01");
        cert.setWriter(new Writer("E00000001"));
        service.registerCustomerCert(cert);
        return cert;
    }
    
    @Test
    public void testRegisterCustomerCert() {
        CustomerCert cert = registerCustomerCert();
        assertNotNull(cert);
        
    }
    
    @Test
    public void testModifyCustomerCert() {
        CustomerCert cert = registerCustomerCert();
        cert.setSerialNumber("62a3e087166113a89c4198842f5313bc");
        cert.setSubjectDn("CN=KRKEVTOPEN00001, O=Hubject");
        cert.setOcspResponderURL("http://ocsp-qa.hubject.com:8080");
        cert.setStatus("CERT02");
        cert.setWriter(new Writer("E00000001"));
        service.modifyCustomerCert(cert);
        assertNotNull(cert);
        
    }
    
//[{"pcid":"HUBOPENPROVCERT001","emaid":"KRKEVTOPEN00001","xsdMsgDefNamespace":"","contractCertificate":"MIIB9zCCAZ2gAwIBAgIQYqPghxZhE6icQZiEL1MTvDAKBggqhkjOPQQDAjBDMQswCQYDVQQGEwJERTEVMBMGA1UEChMMSHViamVjdCBHbWJIMR0wGwYDVQQDExRNTyBTdWIyIENBIFFBIEcxLjIuMTAeFw0yNDA0MDMxMDEzNTRaFw0yNTA0MTUwMDAwMDBaMCwxEDAOBgNVBAoTB0h1YmplY3QxGDAWBgNVBAMTD0tSS0VWVE9QRU4wMDAwMTBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABGENYnl3J1uXXra1Hcpnw0gvVVssUfPedLSveOQ2ykwbCOhVR1VK6KYagKM5NXGsxovWRRtpFPnRvLjWOjmNohqjgYkwgYYwDwYDVR0TAQH/BAUwAwEBADARBgNVHQ4ECgQIQC6Rz6MuB+owEwYDVR0jBAwwCoAIRS5poTYibEgwOwYIKwYBBQUHAQEELzAtMCsGCCsGAQUFBzABhh9odHRwOi8vb2NzcC1xYS5odWJqZWN0LmNvbTo4MDgwMA4GA1UdDwEB/wQEAwID6DAKBggqhkjOPQQDAgNIADBFAiEAktoh2iYFHswFrfgtZg1yL3OSka6icJE48hbTvGyvhHcCIHxT0VVhck/njELBnNhStUNJacy98daF2ouqOA+uMOMp","certValidFrom":"2024-04-03T10:13:54Z","certValidTo":"2025-04-15T00:00:00Z","defaultContract":true}]
//    62a3e087166113a89c4198842f5313bc
//    
//    19:41:43.124 [main] INFO kr.co.kevit.localcsms.adminweb.util.CertUtil - aiaExtensionValue HEX : 042f302d302b06082b06010505073001861f687474703a2f2f6f6373702d71612e6875626a6563742e636f6d3a38303830 
//    19:41:43.189 [main] INFO kr.co.kevit.localcsms.adminweb.util.CertUtil - OCSP Responder URL: http://ocsp-qa.hubject.com:8080
//    
}
