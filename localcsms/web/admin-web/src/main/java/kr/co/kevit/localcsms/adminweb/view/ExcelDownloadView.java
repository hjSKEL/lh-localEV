/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class ExcelDownloadView extends AbstractXlsView {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDownloadView.class);

    private String template;
    private String fileName;

    public ExcelDownloadView(String template, String fileName) {
        this.template = template;
        this.fileName = fileName;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> modal, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
        OutputStream os = null;
        InputStream is = null;
        Workbook excel = null;
        try {
            // 엑셀 템플릿 파일이 존재하는 위치 (classpath 하위)
            is = new ClassPathResource(template).getInputStream();
            os = response.getOutputStream();
            XLSTransformer transformer = new XLSTransformer();
            excel = transformer.transformXLS(is, modal);
            //excel.setForceFormulaRecalculation(true);
            excel.write(os);
            modal.clear();
            os.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                }
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
            if (excel != null)
                try {
                	excel.close();
                } catch (IOException e) {
                }
        }
    }
}