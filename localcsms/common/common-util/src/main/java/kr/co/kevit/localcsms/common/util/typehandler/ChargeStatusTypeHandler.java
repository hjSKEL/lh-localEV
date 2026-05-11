/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargeStatusType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 5. 27.
 */
public class ChargeStatusTypeHandler extends BaseTypeHandler<ChargeStatusType> {

    @Override
    public ChargeStatusType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return ChargeStatusType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public ChargeStatusType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return ChargeStatusType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public ChargeStatusType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return ChargeStatusType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ChargeStatusType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}