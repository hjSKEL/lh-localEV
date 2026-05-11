/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.ocpp.OcppConfigurationKey;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 5.
 */
public class OcppConfigurationKeyHandler extends BaseTypeHandler<OcppConfigurationKey> {

    @Override
    public OcppConfigurationKey getNullableResult(ResultSet resultSet, String str) throws SQLException {
        for(OcppConfigurationKey key : OcppConfigurationKey.values()) {
            if(key.toString().equals(resultSet.getString(str))) {
                return key;
            }
        }
        return null;
    }

    @Override
    public OcppConfigurationKey getNullableResult(ResultSet resultSet, int i) throws SQLException {
        for(OcppConfigurationKey key : OcppConfigurationKey.values()) {
            if(key.toString().equals(resultSet.getString(i))) {
                return key;
            }
        }
        return null;
    }

    @Override
    public OcppConfigurationKey getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        for(OcppConfigurationKey key : OcppConfigurationKey.values()) {
            if(key.toString().equals(callableStatement.getString(i))) {
                return key;
            }
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, OcppConfigurationKey type, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.toString());
    }

}