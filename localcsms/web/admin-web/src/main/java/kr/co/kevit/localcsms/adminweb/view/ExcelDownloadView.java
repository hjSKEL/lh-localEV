/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
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

    /**
     * modal 안의 "excelData" 리스트 각 항목에 1부터 시작하는 순번(seq)을 채워 넣는다.
     * 엑셀 템플릿에서는 ${excelData.seq}로 참조한다.
     * 항목이 Map이면 그대로 복사해서 seq만 채우고, 빈(bean)이면 속성을 Map으로 풀어낸 뒤 seq를 추가한다
     * (JXLS가 Map 속성은 key로, bean 속성은 getter로 읽는 방식이 같이 동작하도록 하기 위함).
     */
    @SuppressWarnings("unchecked")
    private void addSequenceNumbers(Map<String, Object> modal) {
        Object excelData = modal.get("excelData");
        if (!(excelData instanceof List)) {
            return;
        }
        List<?> source = (List<?>) excelData;
        List<Object> numbered = new ArrayList<>(source.size());
        int seq = 1;
        for (Object item : source) {
            try {
                Map<String, Object> row = (item instanceof Map)
                        ? new LinkedHashMap<>((Map<String, Object>) item)
                        : new LinkedHashMap<>(PropertyUtils.describe(item));
                row.put("seq", seq++);
                numbered.add(row);
            } catch (Exception e) {
                LOGGER.error("엑셀 순번(seq) 생성 실패", e);
                numbered.add(item);
            }
        }
        modal.put("excelData", numbered);
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> modal, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8"));
        addSequenceNumbers(modal);
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