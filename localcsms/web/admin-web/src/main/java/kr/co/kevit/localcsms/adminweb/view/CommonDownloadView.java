/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.view;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import kr.co.kevit.localcsms.common.domain.FrameworkFile;
import kr.co.kevit.localcsms.common.util.file.FileUtil;

/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class CommonDownloadView extends AbstractView {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDownloadView.class);

    private FrameworkFile downloadFile;

    public CommonDownloadView(FrameworkFile file) {

        setContentType("application/octet-stream");
        downloadFile = file;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        //
        this.setResponseContentType(request, response);

        java.io.File storageFile = FileUtil.getInstance().getFile(downloadFile.getStorage().getLocation(),
                downloadFile.getStorage().getFileName());
        if (!storageFile.exists()) {
            throw new Exception("file Not exist");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("request Download Storage File is [{}]...", storageFile);
        }

        this.setDownloadFileInfo(downloadFile, request, response);
        response.setContentLength((int) storageFile.length());

        this.setDownloadFile(storageFile, request, response);
    }

    private void setDownloadFileInfo(FrameworkFile downloadFile, HttpServletRequest request,
            HttpServletResponse response) throws UnsupportedEncodingException {
        //
        String fileName = downloadFile.getName();
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
    }

    private void setDownloadFile(java.io.File file, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //
        OutputStream out = response.getOutputStream();
        try (FileInputStream in = new FileInputStream(file)) {
            FileCopyUtils.copy(in, out);
            out.flush();
        }
    }
}
