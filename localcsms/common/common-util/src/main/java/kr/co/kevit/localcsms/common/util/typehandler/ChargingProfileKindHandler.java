package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfileKind;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ChargingProfileKindHandler extends BaseTypeHandler<ChargingProfileKind> {

    @Override
    public ChargingProfileKind getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ChargingProfileKind.getTypeByCode(rs.getString(columnName));
    }

    @Override
    public ChargingProfileKind getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ChargingProfileKind.getTypeByCode(rs.getString(columnIndex));
    }

    @Override
    public ChargingProfileKind getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ChargingProfileKind.getTypeByCode(cs.getString(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ChargingProfileKind type,
            JdbcType jdbcType) throws SQLException {
        ps.setString(i, type.getCode());
    }
}
