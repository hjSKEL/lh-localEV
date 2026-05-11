/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.authority.UserRoleType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 2.
 */
public class UserRoleTypeHandler extends BaseTypeHandler<UserRoleType> {

    @Override
    public UserRoleType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return UserRoleType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public UserRoleType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return UserRoleType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public UserRoleType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return UserRoleType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UserRoleType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}