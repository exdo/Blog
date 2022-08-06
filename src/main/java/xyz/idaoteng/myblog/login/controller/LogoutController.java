package xyz.idaoteng.myblog.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.idaoteng.auth.annotation.LoginRequired;
import xyz.idaoteng.auth.exceptions.NotSignedInException;
import xyz.idaoteng.auth.login.LoginManager;

@RestController
public class LogoutController {
    private final LoginManager loginManager;

    @Autowired
    public LogoutController(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @PostMapping("/api/logout")
    @LoginRequired
    public void logout() {
        try {
            loginManager.tryLogout();
        } catch (NotSignedInException ignored) {

        }
    }
}
