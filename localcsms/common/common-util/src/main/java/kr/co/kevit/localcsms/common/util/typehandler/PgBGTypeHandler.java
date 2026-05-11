/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.recharger.PgBGType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 20.
 */
public class PgBGTypeHandler extends BaseTypeHandler<PgBGType> {

    @Override
    public PgBGType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return PgBGType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public PgBGType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return PgBGType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public PgBGType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return PgBGType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, PgBGType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}