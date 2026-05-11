/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.authority.DeviceType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 30.
 */
public class DeviceTypeHandler extends BaseTypeHandler<DeviceType> {

    @Override
    public DeviceType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return DeviceType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public DeviceType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return DeviceType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public DeviceType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return DeviceType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DeviceType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}