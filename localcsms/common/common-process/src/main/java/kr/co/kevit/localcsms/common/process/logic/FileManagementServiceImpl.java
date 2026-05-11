/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.process.logic;

import kr.co.kevit.localcsms.common.domain.FileStorage;
import kr.co.kevit.localcsms.common.domain.ManagementFile;
import kr.co.kevit.localcsms.common.entity.FileManagementProvider;
import kr.co.kevit.localcsms.common.process.FileManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 4. 18.
 */
@Service
@Transactional
public class FileManagementServiceImpl implements FileManagementService {
    
    @Autowired
    private FileManagementProvider provider;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ManagementFile retrieveManagementFileById(String id) {
        // 
        return provider.retrieveManagementFileById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ManagementFile> retrieveManagementFileByOwner(String owner) {
        // 
        return provider.retrieveManagementFileByOwner(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ManagementFile> retrieveManagementFileHistoryById(String id) {
        // 
        return provider.retrieveManagementFileHistoryById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ManagementFile retrieveManagementFileHistoryByIdAndSeq(String id, int modifySeq) {
        // 
        return provider.retrieveManagementFileHistoryByIdAndSeq(id, modifySeq);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public FileStorage retrieveFileStorage(String id, int modifySeq) {
        // 
        return provider.retrieveFileStorage(id, modifySeq);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagementFile manageFile(ManagementFile file) {
        // 
        return provider.manageFile(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ManagementFile> manageFiles(List<ManagementFile> files) {
        // 
        return provider.manageFiles(files);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagementFile registManagementFile(ManagementFile file) {
        //
        return provider.registManagementFile(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManagementFile modifyManagementFile(ManagementFile modifyFile) {
        //
        return provider.modifyManagementFile(modifyFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeManagementFile(ManagementFile file) {
        //
        provider.removeManagementFile(file);
    }

}
