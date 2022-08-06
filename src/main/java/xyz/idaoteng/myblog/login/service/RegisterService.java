package xyz.idaoteng.myblog.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.idaoteng.auth.tools.RedisUid;
import xyz.idaoteng.auth.utils.PasswordUtil;
import xyz.idaoteng.myblog.common.dto.ResponseResult;
import xyz.idaoteng.myblog.login.dao.UserMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RegisterService {
    private static final Long SYSTEM_ID = 202207011507L;
    private static final String DEFAULT_ROLE_OF_NEW_USER = "general_user";

    private final UserMapper userMapper;
    private final RedisUid redisUid;

    @Autowired
    public RegisterService(UserMapper userMapper, RedisUid redisUid) {
        this.userMapper = userMapper;
        this.redisUid = redisUid;
    }

    @Transactional
    public ResponseResult register(String newUserName,
                                   String newUserPassword,
                                   String newUserEmail) {
        String name = userMapper.selectName(newUserName);
        if (name != null) {
            return new ResponseResult(203, "用户名已被占用，请另起用户名");
        }

        String salt = PasswordUtil.randomString(8);
        String password = PasswordUtil.encrypt(newUserPassword, salt);
        Long id = Long.parseLong(redisUid.nextId());
        userMapper.addUser(id, newUserName, password, salt, newUserEmail);

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        userMapper.addUserRole(id, DEFAULT_ROLE_OF_NEW_USER, SYSTEM_ID, now);

        return new ResponseResult(200, "注册成功");
    }
}
