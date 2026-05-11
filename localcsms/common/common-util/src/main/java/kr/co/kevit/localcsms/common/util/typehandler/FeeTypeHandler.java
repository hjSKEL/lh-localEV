/*******************************************************************************
 * Copyright(c) 2018 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.product.FeeType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author BCKim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2018. 11. 2.
 */
public class FeeTypeHandler extends BaseTypeHandler<FeeType> {

    @Override
    public FeeType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return FeeType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public FeeType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return FeeType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public FeeType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return FeeType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, FeeType feeType,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, feeType.getCode());
    }

}