/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.entity.dao;

import kr.co.kevit.localcsms.common.domain.Code;
import kr.co.kevit.localcsms.common.shared.CodeSearchCond;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a>
 * @since 2018. 8. 31.
 */
@Repository
public interface CodeMapper {

    public int insertCode(@Param("code") Code code);

    public int updateCode(@Param("code") Code code);

    public Code selectCodeByCode(@Param("code") String code);

    public List<Code> selectCodeByParentCode(@Param("highCode") String highCode);

    public int countCodeByCodeSearchCond(@Param("searchCond") CodeSearchCond searchCond);

    public List<Code> selectCodeByCodeSearchCond(@Param("searchCond") CodeSearchCond searchCond);

    public List<Code> selectSubCodeByParentCodes(@Param("highCodes") List<String> highCodes);
}
