package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.customer.CardCategory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 
 * 
 * @author bckim <a href="mailto:bckim@nextree.co.kr">bckim@nextree.co.kr</a> 
 * @since 2018. 5. 24.
 */
public class BankCompanyHandler extends BaseTypeHandler<CardCategory> {

    @Override
    public CardCategory getNullableResult(ResultSet resultSet, String str) throws SQLException {
        return CardCategory.getTypeByCode(resultSet.getString(str));
    }

    @Override
    public CardCategory getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return CardCategory.getTypeByCode(resultSet.getString(i));
    }

    @Override
    public CardCategory getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return CardCategory.getTypeByCode(callableStatement.getString(i));
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CardCategory company,
            JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, company.getCode());
    }

}