package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * @author chul
 *
 */
public class BooleanStringTypeHandler extends BaseTypeHandler<Boolean> {

    private static final String FALSE_STR = "N";
    private static final String TRUE_STR = "Y";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Boolean aBoolean, JdbcType jdbcType)
            throws SQLException {
        preparedStatement.setString(i, aBoolean ? TRUE_STR : FALSE_STR);
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getBoolean(resultSet.getString(s));
    }

    @Override
    public Boolean getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getBoolean(resultSet.getString(i));
    }

    @Override
    public Boolean getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getBoolean(callableStatement.getString(i));
    }

    private Boolean getBoolean(String s) {
        return TRUE_STR.equalsIgnoreCase(s);
    }

}
