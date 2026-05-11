/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import kr.co.kevit.localcsms.common.util.date.DateUtils;
import kr.co.kevit.localcsms.common.util.enumtype.PropertyKey;
import kr.co.kevit.localcsms.common.util.loader.PropertyLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.web.multipart.MultipartFile;


/**
 * 
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 12. 14.
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private static String FILE_SEPARATOR;
    private static final String SEQUENCE_SEPARATOR = "_";
    private static final String EXTENSION_SEPARATOR = ".";

    private static String ROOT_LOCATION;
    private static String TMP_FOLDER;
    private static String Storage_FOLDER;

    private static final int OUTPUT_BUFFER_SIZE = 1024;

    private static FileUtil instance = null;

    public FileUtil() {
        LOGGER.info("Init FileUtil...");

        String fileSeparator = File.separator;
        // 파일 구분자가 [\]일경우 replace 오류 방지
        if (fileSeparator.equals("\\")) {
            fileSeparator = "\\\\";
        }

        ROOT_LOCATION = PropertyLoader.getInstance().getProperty(PropertyKey.RootFolderLocation);
        ROOT_LOCATION = ROOT_LOCATION.replaceAll("/", fileSeparator);

        TMP_FOLDER = PropertyLoader.getInstance().getProperty(PropertyKey.TmpFolderLocation);
        TMP_FOLDER = TMP_FOLDER.replaceAll("/", fileSeparator);
        isFolderExist(ROOT_LOCATION + TMP_FOLDER);

        Storage_FOLDER = PropertyLoader.getInstance().getProperty(PropertyKey.StorageFolderLocation);
        Storage_FOLDER = Storage_FOLDER.replaceAll("/", fileSeparator);
        isFolderExist(ROOT_LOCATION + Storage_FOLDER);

        FILE_SEPARATOR = File.separator;

        LOGGER.info("=====================================================");
        LOGGER.info("Tmp Folder Location : {}", ROOT_LOCATION + TMP_FOLDER);
        LOGGER.info("Storage Folder Location : {}", ROOT_LOCATION + Storage_FOLDER);
        LOGGER.info("=====================================================");
    }

    private static boolean isFolderExist(String path) {

        File file = new File(path);
        if (file.exists()) {
            return file.isDirectory();
        }
        return file.mkdirs();
    }

    public static FileUtil getInstance() {

        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    public String getPath(String storageFolderLocation, String fileName) {
        //
        StringBuilder builder = new StringBuilder(ROOT_LOCATION);

        if (StringUtils.isEmpty(storageFolderLocation)) {
            storageFolderLocation = TMP_FOLDER;
        }

        builder.append(storageFolderLocation);
        if (!isFolderExist(builder.toString())) {
            // TODO exception
            LOGGER.error("make Folder fail...");
            return null;
        }

        builder.append(FILE_SEPARATOR).append(fileName);
        return builder.toString();
    }

    public boolean delete(File file) {
        //
        if (!file.exists()) {
            LOGGER.error("file Not Exist {0}", file.getPath());
            return false;
        }
        return file.delete();
    }

    public boolean write(File sourceFile, String destinationPath) {
        //
        File destinationFile = new File(destinationPath);

        if (destinationFile.exists()) {
            LOGGER.error("file is already Exist {0}", destinationPath);
            return false;
        }

        try (FileInputStream inputStream = new FileInputStream(sourceFile);
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    public String getROOT_LOCATION() {
        return ROOT_LOCATION;
    }

    public String getTMP_FOLDER() {
        return TMP_FOLDER;
    }

    public String getStorage_FOLDER() {
        return Storage_FOLDER;
    }
    
    public void moveToAnotherStorage(File currentFile, String location, String fileName) {
        String movePath = getPath(location, fileName);
        write(currentFile, movePath);
    }

    public void deleteFile(String location, String fileName) {
        //
        File currentFile = this.getFile(location, fileName);
        delete(currentFile);
    }

    public File getFile(String location, String fileName) {
        //
        String filePath = getPath(location, fileName);
        File storageFile = new File(filePath);
        return storageFile;
    }
    
    public String getDir(String id, int modifySeq) {

        StringBuilder locationBuilder = new StringBuilder(Storage_FOLDER);
        locationBuilder.append(FILE_SEPARATOR).append(DateUtils.getCurrentDateAsString(DateUtils.DATE_FORMAT_WITHOUT_DASH));

        return locationBuilder.toString();
    }
    
    public String getFileName(String id, String kind, int modifySeq) {

        StringBuilder storageFileNameBuilder = new StringBuilder(id);
        storageFileNameBuilder.append(SEQUENCE_SEPARATOR).append(modifySeq);
        storageFileNameBuilder.append(EXTENSION_SEPARATOR).append(kind);
        return storageFileNameBuilder.toString();
    }
}
