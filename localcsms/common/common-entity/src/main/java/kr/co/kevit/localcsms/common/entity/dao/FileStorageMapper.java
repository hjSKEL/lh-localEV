/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import kr.co.kevit.localcsms.common.domain.FileStorage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author mbkim <a href="mailto:mbkim@nextree.co.kr">mbkim@nextree.co.kr</a>
 * @since 2018. 5. 4.
 */
@Repository
public interface FileStorageMapper {

    public FileStorage selectFileStorageByIdAndSeq(@Param("id") String id, @Param("modifySeq") int modifySeq);

    public void insertFileStorage(@Param("id") String id, @Param("modifySeq") int modifySeq, @Param("fileStorage") FileStorage fileStorage);
}
