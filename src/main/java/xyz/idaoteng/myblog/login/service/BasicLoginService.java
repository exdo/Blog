package xyz.idaoteng.myblog.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.idaoteng.auth.custom.BasicLoginProcess;
import xyz.idaoteng.auth.subject.UserInfo;
import xyz.idaoteng.auth.utils.PasswordUtil;
import xyz.idaoteng.myblog.login.dao.UserMapper;

import java.util.List;

@Component
public class BasicLoginService implements BasicLoginProcess {
    private final UserMapper userMapper;

    @Autowired
    public BasicLoginService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserInfo getUserInfo(String name) {
        UserInfo userInfo = userMapper.selectUserByName(name);
        if (userInfo == null) return null;
        List<String> roles = userMapper.selectRoles(userInfo.getId());
        userInfo.setRoles(roles);
        List<String> permissions = userMapper.selectPermissions(roles);
        userInfo.setPermissions(permissions);
        return userInfo;
    }

    @Override
    public boolean matchPasswords(UserInfo userInfo, String uploadedPassword) {
        String password = PasswordUtil.encrypt(uploadedPassword, userInfo.getSalt());
        return userInfo.getPassword().equals(password);
    }
}
