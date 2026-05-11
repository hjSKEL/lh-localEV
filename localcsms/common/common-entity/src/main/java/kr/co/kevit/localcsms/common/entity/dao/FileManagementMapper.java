/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import kr.co.kevit.localcsms.common.domain.ManagementFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author mbkim <a href="mailto:mbkim@nextree.co.kr">mbkim@nextree.co.kr</a>
 * @since 2018. 5. 4.
 */
@Repository
public interface FileManagementMapper {

    public ManagementFile selectManagementFileById(@Param("id") String id);

    public List<ManagementFile> selectManagementFileByOwner(@Param("owner") String owner);

    public List<ManagementFile> selectManagementFileHistoryById(@Param("id") String id);

    public ManagementFile selectManagementFileHistoryByIdAndSeq(@Param("id") String id, @Param("modifySeq") int modifySeq);

    public void insertManagementFile(@Param("file") ManagementFile file);

    public void updateManagementFile(@Param("file") ManagementFile file);

    public void deleteManagementFile(@Param("id") String id, @Param("modifySeq") int modifySeq);

}
