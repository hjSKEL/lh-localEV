package kr.co.kevit.localcsms.common.util.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * CHAR(1) 'Y'/'N' ↔ Boolean. NULL 을 보존한다 (NULL → null).
 *
 * <p>{@link BooleanStringTypeHandler} 와 달리 NULL 컬럼을 {@code false} 가 아닌 {@code null} 로
 * 반환한다. OCPP 2.1 의 3-state(미지정/true/false) boolean 필드(useLocalTime, evseSleep,
 * preconditioningRequest, invalidAfterOfflineDuration)에 사용 — 미지정 시 직렬화에서 omit 되어야 함.</p>
 */
public class NullableYnBooleanTypeHandler extends BaseTypeHandler<Boolean> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter ? "Y" : "N");
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toBoolean(rs.getString(columnName));
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toBoolean(rs.getString(columnIndex));
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toBoolean(cs.getString(columnIndex));
    }

    private Boolean toBoolean(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return "Y".equalsIgnoreCase(s.trim());
    }
}
