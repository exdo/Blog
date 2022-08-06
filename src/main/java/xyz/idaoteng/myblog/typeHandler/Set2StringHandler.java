package xyz.idaoteng.myblog.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
   mybatis没有对Set<String>类型的数据进行自动映射处理，
   下面的处理方式是将Set类型的对象映射成Arrays.toString打印格式的字符串
   即先将Set<String>变成String[]再按照Arrays.toString的打印格式写入到数据库
   从数据库取回时将字符串分割成String[]，再装配到Set<String>
 */
@MappedTypes(value = Set.class)
@MappedJdbcTypes(value = {JdbcType.VARCHAR})
public class Set2StringHandler implements TypeHandler<Set<String>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setString(i, "");
            return;
        }
        String psParameter = parseToString(parameter);
        ps.setString(i, psParameter);
    }

    @Override
    public Set<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        if ("".equals(result) || result == null) {
            return null;
        }
        return parseToSet(result);
    }

    @Override
    public Set<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        if ("".equals(result) || result == null) {
            return null;
        }
        return parseToSet(result);
    }

    @Override
    public Set<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        if ("".equals(result) || result == null) {
            return null;
        }
        return parseToSet(result);
    }
    //-------------------------转换过程-----------------------------------
    private String parseToString(Set<String> parameter) {
        return Arrays.toString(parameter.toArray());
    }

    private Set<String> parseToSet(String result) {
        String temp = result.substring(1, result.length() -1);
        String[] strings = temp.split(", ");
        return new HashSet<>(Arrays.asList(strings));
    }
}
