package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @author chul
 *
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    
    @Override
    public Date getNullableResult(ResultSet rs, String columnName)  throws SQLException {
        java.sql.Date sqlDate = rs.getDate(columnName);
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }
    
    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if(resultSet.getDate(i) != null)
            return new Date(resultSet.getDate(i).getTime());
        else
            return null;
    }
    
    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if(callableStatement.getDate(i) != null)
            return new Date(callableStatement.getDate(i).getTime());
        else
            return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType)
            throws SQLException {
        if(date != null)
            preparedStatement.setDate(i, new java.sql.Date(date.getTime()));
        else
            preparedStatement.setDate(i, null);
    }

}
