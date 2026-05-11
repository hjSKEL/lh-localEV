/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.charger.RecurrencyKindType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2020. 6. 12.
 */
public class RecurrencyKindTypeHandler extends BaseTypeHandler<RecurrencyKindType> {

    @Override
    public RecurrencyKindType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return RecurrencyKindType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public RecurrencyKindType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return RecurrencyKindType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public RecurrencyKindType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return RecurrencyKindType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, RecurrencyKindType type, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}