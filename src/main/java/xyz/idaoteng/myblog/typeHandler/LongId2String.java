package xyz.idaoteng.myblog.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(value = String.class)
@MappedJdbcTypes(value = {JdbcType.BIGINT})
public class LongId2String implements TypeHandler<String> {

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, Long.parseLong(parameter));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        long id = rs.getLong(columnName);
        return Long.toString(id);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        long id = rs.getLong(columnIndex);
        return Long.toString(id);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        long id = cs.getLong(columnIndex);
        return Long.toString(id);
    }
}
