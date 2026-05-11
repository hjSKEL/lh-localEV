/*******************************************************************************
 * Copyright(c) 2019 K-AEA All rights reserved.
 * This software is the proprietary information of K-AEA
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import kr.co.kevit.localcsms.common.util.enumtype.organization.TaskType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 9. 20.
 */
public class TaskTypeHandler extends BaseTypeHandler<TaskType> {

    @Override
    public TaskType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return TaskType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public TaskType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return TaskType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public TaskType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return TaskType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, TaskType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}