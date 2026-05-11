/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.adminweb.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import kr.co.kevit.localcsms.common.domain.FileStorage;
import kr.co.kevit.localcsms.common.domain.FrameworkFile;
import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;
import kr.co.kevit.localcsms.common.util.file.FileUtil;
import kr.co.kevit.localcsms.common.util.loader.PropertyLoader;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2019. 4. 18.
 */
public class WebFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebFileUtil.class);

    private static WebFileUtil instance = new WebFileUtil();

    public WebFileUtil() {
    }

    public static WebFileUtil getInstance() {
        return instance;
    }

    public FrameworkFile storeFileToCheckListLocation(MultipartFile uploadFile, String dir, String name) {

        FrameworkFile file = new FrameworkFile();

        FileStorage storage = getCheckListStorage(dir, name);
        try {
            uploadFile.transferTo(FileUtil.getInstance().getFile(storage.getLocation(), storage.getFileName()));
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String fileName = uploadFile.getOriginalFilename();
        file.setName(fileName);
        file.setKind(fileName.substring(fileName.lastIndexOf('.') + 1));
        file.setSize(uploadFile.getSize());
        file.setStorage(storage);

        return file;
    }

    public FrameworkFile storeFileToOcppLocation(MultipartFile uploadFile, String dir, String name) {

        FrameworkFile file = new FrameworkFile();

        FileStorage storage = getOcppStorage(dir, name);
        try {
            uploadFile.transferTo(FileUtil.getInstance().getFile(storage.getLocation(), storage.getFileName()));
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String fileName = uploadFile.getOriginalFilename();
        file.setName(fileName);
        file.setKind(fileName.substring(fileName.lastIndexOf('.') + 1));
        file.setSize(uploadFile.getSize());
        file.setStorage(storage);
        return file;
    }

    private FileStorage getOcppStorage(String dir, String fileName) {
        //
        StringBuffer buffer = new StringBuffer(256);
        buffer.append(PropertyLoader.getInstance().getProperty(PropertyKey.OcppFolderLocation));
        buffer.append(File.separator);
        buffer.append(dir);
        return new FileStorage(buffer.toString(), fileName);
    }

    public FrameworkFile storeFileToTmpLocation(MultipartFile uploadFile, String register) {

        FrameworkFile file = new FrameworkFile();

        FileStorage storage = getTmpStorage();
        try {
            uploadFile.transferTo(FileUtil.getInstance().getFile(storage.getLocation(), storage.getFileName()));
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String fileName = uploadFile.getOriginalFilename();
        file.setName(fileName);
        file.setKind(fileName.substring(fileName.lastIndexOf('.') + 1));
        file.setSize(uploadFile.getSize());
        file.setStorage(storage);

        return file;
    }

    private FileStorage getCheckListStorage(String dir, String fileName) {
        //
        StringBuffer buffer = new StringBuffer(256);
        buffer.append(PropertyLoader.getInstance().getProperty(PropertyKey.CheckListFolderLocation));
        buffer.append(File.separator);
        buffer.append(dir);
        return new FileStorage(buffer.toString(), fileName);
    }

    private FileStorage getTmpStorage() {
        //
        String storageFileName = DateUtils.getCurrentDateAsString(DateUtils.YYYYMMDDHHMMSS);
        return new FileStorage(PropertyLoader.getInstance().getProperty(PropertyKey.TmpFolderLocation),
                storageFileName);
    }
}
