package xyz.idaoteng.myblog.login.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.idaoteng.auth.subject.UserInfo;

import java.util.List;

@Mapper
public interface UserMapper {
    UserInfo selectUserByName(String name);

    @Select("select name from user where name=#{name}")
    String selectName(String name);

    List<String> selectRoles(String id);

    List<String> selectPermissions(@Param("roleList") List<String> roles);

    int addUser(@Param("id") Long id,
                @Param("name") String name,
                @Param("password") String password,
                @Param("salt") String salt,
                @Param("email") String email);

    @Insert("insert into user_role (user_id, role_name, grantor_id, grant_time) " +
            "values (#{userId}, #{roleName}, #{grantorId}, #{grantTime})")
    void addUserRole(@Param("userId") Long userId,
                    @Param("roleName") String roleName,
                    @Param("grantorId") Long grantorId,
                    @Param("grantTime") String grantTime);
}
