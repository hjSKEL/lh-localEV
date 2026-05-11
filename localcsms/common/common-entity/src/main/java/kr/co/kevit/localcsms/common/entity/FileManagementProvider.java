/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity;

import kr.co.kevit.localcsms.common.domain.FileStorage;
import kr.co.kevit.localcsms.common.domain.ManagementFile;

import java.util.List;

/**
 * 파일 저장소 관리 Provider
 * 
 * <pre>
 * 1. 파일 등록전, 실제 파일은 FileUtil을 통해 임시 저상소에 등록이 되어있어야한다.
 * 2. 파일 저장소 등록시 각각의 서비스에서 Id(소유자 Id)가 채번이 되어있어야된다.
 * </pre>
 * 
 * @author mbkim <a href="mailto:mbkim@nextree.co.kr">mbkim@nextree.co.kr</a>
 * @since 2018. 5. 15.
 */
public interface FileManagementProvider {
    //
    /**
     * 파일 Id로 저장소 파일 조회. - 가장 마지막에 수정된 파일을 조회한다.
     * 
     * @param id
     * @return
     */
    ManagementFile retrieveManagementFileById(String id);

    /**
     * 소유자Id로 파일목록 조회.
     * 
     * @param owner
     * @return
     */
    List<ManagementFile> retrieveManagementFileByOwner(String owner);

    /**
     * 파일 Id에 해당되는 수정 이력정보를 조회한다.
     * 
     * @param id
     * @return
     */
    List<ManagementFile> retrieveManagementFileHistoryById(String id);

    /**
     * 파일 Id와 수정 순번에 해당되는 파일 정보를 조회한다.
     * 
     * @param id
     * @param modifySeq
     * @return
     */
    ManagementFile retrieveManagementFileHistoryByIdAndSeq(String id, int modifySeq);

    /**
     * 파일 Id에 해당되는 저장소 정보를 조회한다. - 실제파일 저장위치
     * 
     * @param id
     * @param modifySeq
     * @return
     */
    FileStorage retrieveFileStorage(String id, int modifySeq);

    /**
     * 
     * @param file
     * @return
     */
    ManagementFile manageFile(ManagementFile file);

    /**
     * @param files
     * @return
     */
    List<ManagementFile> manageFiles(List<ManagementFile> files);

    /**
     * 파일 저장소 등록
     * 
     * @param file
     * @return
     */
    ManagementFile registManagementFile(ManagementFile file);

    /**
     * 파일 수정
     * 
     * <pre>
     * 1. 실제 파일이 수정되었을경우, 수정된 파일 기준으로 수정정보를 신규 등록한다.
     * 2. 파일 정보는 어떠한 일이 있어도 수정하지 않는다.
     * </pre>
     * 
     * @param modifyFile
     * @return
     */
    ManagementFile modifyManagementFile(ManagementFile modifyFile);

    /**
     * 파일 삭제
     * 
     * @param file
     */
    void removeManagementFile(ManagementFile file);
}
