/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import kr.co.kevit.localcsms.common.util.enumtype.customer.TargetType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 2.
 */
public class TargetTypeHandler extends BaseTypeHandler<TargetType> {

    @Override
    public TargetType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return TargetType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public TargetType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return TargetType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public TargetType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return TargetType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, TargetType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}