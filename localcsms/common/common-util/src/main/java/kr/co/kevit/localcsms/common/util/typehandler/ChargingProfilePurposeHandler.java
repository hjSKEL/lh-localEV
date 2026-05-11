package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kevit.localcsms.common.util.enumtype.charger.ChargingProfilePurpose;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ChargingProfilePurposeHandler extends BaseTypeHandler<ChargingProfilePurpose> {

    @Override
    public ChargingProfilePurpose getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ChargingProfilePurpose.getTypeByCode(rs.getString(columnName));
    }

    @Override
    public ChargingProfilePurpose getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ChargingProfilePurpose.getTypeByCode(rs.getString(columnIndex));
    }

    @Override
    public ChargingProfilePurpose getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ChargingProfilePurpose.getTypeByCode(cs.getString(columnIndex));
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ChargingProfilePurpose type,
            JdbcType jdbcType) throws SQLException {
        ps.setString(i, type.getCode());
    }
}
