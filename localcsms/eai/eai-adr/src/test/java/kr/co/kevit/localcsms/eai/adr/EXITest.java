/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr;

import java.util.Base64;

import org.junit.Test;

import kr.co.kevit.localcsms.eai.adr.util.EXIUtils;
/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 28.
 */
public class EXITest {

    @Test
    public void test() throws Exception{
        // XML 데이터
        String xmlData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
                "<DERControlResponse xmlns=\"urn:ieee:std:2030.5:ns\">\r\n" + 
                "    <createdDateTime>1341507000</createdDateTime>\r\n" + 
                "    <endDeviceLFDI>C0FFEE00</endDeviceLFDI>\r\n" + 
                "    <status>1</status>\r\n" + 
                "    <subject>02BE7A7E57</subject>\r\n" + 
                "</DERControlResponse>";

        // XML -> EXI
        byte[] exiData = EXIUtils.encodeToEXI(xmlData);
        System.out.println("EXI Encoded Data Length: " + exiData.length);
        
        byte [] resultBase64 = Base64.getEncoder().encode(exiData);
        System.out.println(new String(resultBase64));
        
        // EXI -> XML
        String decodedXml = EXIUtils.decodeFromEXI(exiData);
        System.out.println("Decoded XML:\n" + decodedXml);
    }
}
