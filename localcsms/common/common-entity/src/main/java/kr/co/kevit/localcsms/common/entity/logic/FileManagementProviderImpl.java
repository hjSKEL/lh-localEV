/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.logic;

import kr.co.kevit.localcsms.common.domain.FileStorage;
import kr.co.kevit.localcsms.common.domain.FrameworkFile;
import kr.co.kevit.localcsms.common.domain.ManagementFile;
import kr.co.kevit.localcsms.common.util.file.FileUtil;
import kr.co.kevit.localcsms.common.entity.FileManagementProvider;
import kr.co.kevit.localcsms.common.entity.dao.FileManagementMapper;
import kr.co.kevit.localcsms.common.entity.dao.FileStorageMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mbkim <a href="mailto:mbkim@nextree.co.kr">mbkim@nextree.co.kr</a>
 * @since 2018. 5. 4.
 */
@Component
public class FileManagementProviderImpl implements FileManagementProvider {

    @Autowired
    private FileManagementMapper fileManagementMapper;
    
    @Autowired
    private FileStorageMapper fileStorageMapper;

    @Override
    public ManagementFile retrieveManagementFileById(String id) {
        //
        return fileManagementMapper.selectManagementFileById(id);
    }

    @Override
    public List<ManagementFile> retrieveManagementFileByOwner(String owner) {
        //
        return fileManagementMapper.selectManagementFileByOwner(owner);
    }

    @Override
    public List<ManagementFile> retrieveManagementFileHistoryById(String id) {
        //
        return fileManagementMapper.selectManagementFileHistoryById(id);
    }

    @Override
    public ManagementFile retrieveManagementFileHistoryByIdAndSeq(String id, int modifySeq) {
        //
        return fileManagementMapper.selectManagementFileHistoryByIdAndSeq(id, modifySeq);
    }

    @Override
    public FileStorage retrieveFileStorage(String id, int modifySeq) {
        //
        return fileStorageMapper.selectFileStorageByIdAndSeq(id, modifySeq);
    }

    @Override
    public ManagementFile manageFile(ManagementFile file) {
        //
        switch (file.getManagementOperator()) {
        case Create:
            return registManagementFile(file);
        case Update:
            return modifyManagementFile(file);
        case Delete:
            removeManagementFile(file);
            break;
        case Copy:
            break;
        default:
            break;
        }

        return null;
    }

    @Override
    public List<ManagementFile> manageFiles(List<ManagementFile> files) {
        //
        List<ManagementFile> operationCompliteFiles = new ArrayList<ManagementFile>();

        int fileOrder = 0;
        for (ManagementFile file : files) {
            ManagementFile operationCompliteFile = manageFile(file);
            if (operationCompliteFile != null) {
                operationCompliteFile.setOrder(fileOrder++);
                operationCompliteFiles.add(operationCompliteFile);
            }
        }

        return operationCompliteFiles;
    }

    @Override
    public ManagementFile registManagementFile(ManagementFile file) {
        //
        String fileId = file.getOwner() + file.getOrder();
        File orgFile = FileUtil.getInstance().getFile(file.getStorage().getLocation(), file.getStorage().getFileName());
        FileStorage newStorage = getStorage(fileId, file, file.getModifySeq());
        FileUtil.getInstance().moveToAnotherStorage(orgFile, newStorage.getLocation(), newStorage.getFileName());

        file.setId(fileId);
        fileManagementMapper.insertManagementFile(file);
        fileStorageMapper.insertFileStorage(file.getId(), file.getModifySeq(), file.getStorage());

        return file;
    }

    private FileStorage getStorage(String id, FrameworkFile file, int modifySeq) {
        //
        return new FileStorage(FileUtil.getInstance().getDir(id, modifySeq),
                FileUtil.getInstance().getFileName(id, file.getKind(), modifySeq));
    }

    @Override
    public ManagementFile modifyManagementFile(ManagementFile modifyFile) {

        FileUtil fileUtil = FileUtil.getInstance();

        String fileId = modifyFile.getId();
        ManagementFile originFile = fileManagementMapper.selectManagementFileById(fileId);

        // 기존 File 정보 없을시 신규 등록
        if (originFile == null) {
            return registManagementFile(modifyFile);
        }
        File orgFile = fileUtil.getFile(originFile.getStorage().getLocation(), originFile.getStorage().getFileName());
        FileStorage newStorage = getStorage(fileId, modifyFile, modifyFile.getNextModifySeq());
        fileUtil.moveToAnotherStorage(orgFile, newStorage.getLocation(), newStorage.getFileName());

        fileManagementMapper.updateManagementFile(modifyFile);
        fileStorageMapper.insertFileStorage(modifyFile.getId(), modifyFile.getModifySeq(), modifyFile.getStorage());

        return modifyFile;
    }

    // ======= 파일삭제 ===================
    // 엄부 정의 후, 실제 파일 삭제할지 상태값 수정할지 적용 필요
    // ================================
    @Override
    public void removeManagementFile(ManagementFile file) {
        //
        File storageFile = FileUtil.getInstance().getFile(file.getStorage().getLocation(),
                file.getStorage().getFileName());
        FileUtil.getInstance().delete(storageFile);
        fileManagementMapper.deleteManagementFile(file.getId(), file.getModifySeq());
    }
}
