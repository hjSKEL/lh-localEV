package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * java.util.Date 전역 TypeHandler.
 *
 * <p>type-handlers-package 로 등록되어 모든 java.util.Date 컬럼의 기본 핸들러로 동작한다.
 * 시:분:초를 보존하기 위해 java.sql.Date(날짜만) 가 아니라 java.sql.Timestamp(날짜+시간) 로
 * 읽고 쓴다. DB 컬럼이 DATE 타입이면 시간 부분은 자연히 절삭되므로 부작용이 없다.
 *
 * @author chul
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnName);
        return ts != null ? new Date(ts.getTime()) : null;
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Timestamp ts = resultSet.getTimestamp(i);
        return ts != null ? new Date(ts.getTime()) : null;
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Timestamp ts = callableStatement.getTimestamp(i);
        return ts != null ? new Date(ts.getTime()) : null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType)
            throws SQLException {
        preparedStatement.setTimestamp(i, new Timestamp(date.getTime()));
    }

}
