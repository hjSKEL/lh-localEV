/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.ocpp.OCPPMsgDirectType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2019. 7. 4.
 */
public class OCPPMsgDirectTypeHandler extends BaseTypeHandler<OCPPMsgDirectType> {

    @Override
    public OCPPMsgDirectType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return OCPPMsgDirectType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public OCPPMsgDirectType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return OCPPMsgDirectType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public OCPPMsgDirectType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return OCPPMsgDirectType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, OCPPMsgDirectType type, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}