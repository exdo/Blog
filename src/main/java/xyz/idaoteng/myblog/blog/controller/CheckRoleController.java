package xyz.idaoteng.myblog.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.idaoteng.auth.annotation.BindRoles;
import xyz.idaoteng.auth.annotation.LoginRequired;
import xyz.idaoteng.myblog.common.dto.ResponseResult;

@RestController
public class CheckRoleController {
    @GetMapping("/api/isRoot")
    @BindRoles("root_user")
    public ResponseResult isRoot() {
        return new ResponseResult(200);
    }

    @GetMapping("/api/alreadyLogged")
    @LoginRequired
    public ResponseResult alreadyLogged() {
        return new ResponseResult(200);
    }
}