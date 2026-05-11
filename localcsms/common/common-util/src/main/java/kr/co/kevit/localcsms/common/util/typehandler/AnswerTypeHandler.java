/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.inspection.AnswerType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 10.
 */
public class AnswerTypeHandler extends BaseTypeHandler<AnswerType> {

    @Override
    public AnswerType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return AnswerType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public AnswerType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return AnswerType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public AnswerType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return AnswerType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, AnswerType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}