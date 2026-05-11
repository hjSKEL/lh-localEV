/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import kr.co.kevit.localcsms.common.domain.CodeVal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Repository
public interface CodeValMapper {

    public int insertCodeVal(@Param("codeVal") CodeVal codeVal);

    public int updateCodeVal(@Param("codeVal") CodeVal codeVal);

    public CodeVal selectCodeValByCode(@Param("code") String code);

    public List<CodeVal> selectCodeValByParentCode(@Param("highCode") String highCode);

}
