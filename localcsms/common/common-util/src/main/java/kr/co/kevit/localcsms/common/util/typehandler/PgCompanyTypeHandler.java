package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.recharger.PgCompanyType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 24.
 */
public class PgCompanyTypeHandler extends BaseTypeHandler<PgCompanyType> {

    @Override
    public PgCompanyType getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return PgCompanyType.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public PgCompanyType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return PgCompanyType.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public PgCompanyType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return PgCompanyType.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, PgCompanyType type,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, type.getCode());
    }

}