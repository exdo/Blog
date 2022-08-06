package xyz.idaoteng.myblog.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.auth.exceptions.NoReceiptException;
import xyz.idaoteng.auth.exceptions.NoSuchUserException;
import xyz.idaoteng.auth.exceptions.OverfastRequestException;
import xyz.idaoteng.auth.exceptions.WrongPasswordException;
import xyz.idaoteng.auth.login.LoginManager;
import xyz.idaoteng.auth.login.LoginResult;
import xyz.idaoteng.auth.receipt.Receipt;
import xyz.idaoteng.auth.receipt.ReceiptService;
import xyz.idaoteng.myblog.common.dto.ResponseResult;
import xyz.idaoteng.myblog.login.entity.dto.LoginData;

@RestController
@Slf4j
@RequestMapping("/api/login")
public class LoginController {
    private final LoginManager loginManager;
    private final ReceiptService receiptService;

    @Autowired
    public LoginController(LoginManager loginManager, ReceiptService receiptService) {
        this.loginManager = loginManager;
        this.receiptService = receiptService;
    }

    @GetMapping("/receipt")
    public ResponseResult getTicket() {
        Receipt receipt = receiptService.nextReceipt();
        log.debug("Receipt ID = {}", receipt.getReceiptId());
        return new ResponseResult().setCode(200).setContent(receipt);
    }

    @GetMapping("/captcha")
    public ResponseResult getCaptcha(@RequestHeader("Receipt") String receiptId) {
        //更换验证码
        try {
            Receipt receipt = receiptService.refreshVerificationCode(receiptId);
            return new ResponseResult().setCode(200).setContent(receipt.getOutputObject());
        } catch (OverfastRequestException | NoReceiptException e) {
            return new ResponseResult().setCode(403).setMessage(e.getMessage());
        }
    }

    @PostMapping
    public ResponseResult login(@RequestBody @Validated LoginData loginData, @RequestHeader("Receipt") String receiptId) {
        //校验验证码
        boolean pass;
        try {
            pass = receiptService.validateVerificationCode(receiptId, loginData.getFeedback());
        } catch (NoReceiptException e) {
            return new ResponseResult().setCode(600).setMessage("没有有效的receipt id");
        }
        if (!pass) {
            return new ResponseResult().setCode(403).setMessage("验证码错误或过期");
        }

        try {
            log.debug("用户名 = {}", loginData.getName());
            LoginResult result = loginManager.tryLogin(loginData.getName(), loginData.getPassword());
            return new ResponseResult().setCode(200).setMessage("登入成功").setContent(result.getAuthToken());
        } catch (NoSuchUserException | WrongPasswordException e) {
            return new ResponseResult().setCode(403).setMessage(e.getMessage());
        }
    }

    @GetMapping("/rememberMe")
    public ResponseResult rememberMe(@RequestHeader("JwtToken") String JwtToken) {
        try {
            LoginResult result = loginManager.rememberMe(JwtToken);
            if (result.getSuccess()) {
                return new ResponseResult(200).setContent(result.getRememberMeToken());
            } else {
                return new ResponseResult(403);
            }
        } catch (NoSuchUserException | WrongPasswordException e) {
            return new ResponseResult(403);
        }
    }
}
