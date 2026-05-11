/*******************************************************************************
 * Copyright(c) 2016-2020 kevit Corporation. 
 * All rights reserved. This software is the proprietary information of 
 * kevit Corporation.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.charger.RepairType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author chul <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 1. 4.
 */
public class RepairTypeHandler extends BaseTypeHandler<RepairType> {

    @Override
    public RepairType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return RepairType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public RepairType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return RepairType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public RepairType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return RepairType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, RepairType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}