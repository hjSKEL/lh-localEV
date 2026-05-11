/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.roaming.CdrStatusType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 12. 11.
 */
public class CdrStatusTypeHandler extends BaseTypeHandler<CdrStatusType> {

    @Override
    public CdrStatusType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return CdrStatusType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public CdrStatusType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return CdrStatusType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public CdrStatusType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return CdrStatusType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CdrStatusType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}