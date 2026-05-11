/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.adr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.grammars.Grammars;
import com.siemens.ct.exi.core.helpers.DefaultEXIFactory;
import com.siemens.ct.exi.grammars.GrammarFactory;
import com.siemens.ct.exi.main.api.sax.EXIResult;
import com.siemens.ct.exi.main.api.sax.EXISource;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 28.
 */
public class EXIUtils {
    
    private static String SCHEMA_PATH = "src/main/resources/schemas/ieee2030.5.xsd";

    /**
     * XML 데이터를 EXI 형식으로 변환
     *
     * @param xmlData    XML 데이터 문자열
     * @return EXI 형식의 바이트 배열
     */
    public static byte[] encodeToEXI(String xmlData) throws Exception {
        // EXISchema 생성
        GrammarFactory grammarFactory = GrammarFactory.newInstance();
        Grammars grammar = grammarFactory.createGrammars(SCHEMA_PATH);

        // EXIFactory 설정
        EXIFactory exiFactory = DefaultEXIFactory.newInstance();
        exiFactory.setGrammars(grammar);

        EXIResult exiResult = new EXIResult(exiFactory);
        ByteArrayOutputStream bosEXI = new ByteArrayOutputStream();
        exiResult.setOutputStream(bosEXI);
        
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler( exiResult.getHandler() );
        
        xmlReader.parse(new InputSource(new StringReader(xmlData))); // parse XML input
        return bosEXI.toByteArray();
    }

    /**
     * EXI 데이터를 XML로 변환
     *
     * @param exiData    EXI 형식의 바이트 배열
     * @return 복원된 XML 데이터 문자열
     */
    public static String decodeFromEXI(byte[] exiData) throws Exception {
        // EXISchema 생성
        GrammarFactory grammarFactory = GrammarFactory.newInstance();
        Grammars grammar = grammarFactory.createGrammars(SCHEMA_PATH);

        // EXIFactory 설정
        EXIFactory exiFactory = DefaultEXIFactory.newInstance();
        exiFactory.setGrammars(grammar);
        
        InputSource is = new InputSource(new ByteArrayInputStream(exiData));
        
        SAXSource exiSource = new EXISource(exiFactory);
        exiSource.setInputSource(is);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Result res = new StreamResult(outputStream);
        transformer.transform(exiSource, res);  
        
        return new String(outputStream.toByteArray());
    }
}
