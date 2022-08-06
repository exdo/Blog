package xyz.idaoteng.myblog.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.idaoteng.auth.exceptions.NoReceiptException;
import xyz.idaoteng.auth.exceptions.OverfastRequestException;
import xyz.idaoteng.auth.receipt.Receipt;
import xyz.idaoteng.auth.receipt.ReceiptService;
import xyz.idaoteng.myblog.common.dto.ResponseResult;
import xyz.idaoteng.myblog.login.entity.dto.RegisterData;
import xyz.idaoteng.myblog.login.service.RegisterService;

@RestController
@RequestMapping("/api/register")
@Slf4j
public class RegisterController {
    private final RegisterService registerService;
    private final ReceiptService receiptService;

    @Autowired
    public RegisterController(RegisterService registerService, ReceiptService receiptService) {
        this.registerService = registerService;
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

    @PostMapping()
    public ResponseResult register(@RequestBody @Validated RegisterData registerData,
                                   @RequestHeader("Receipt") String receiptId) {
        //校验验证码
        boolean pass;
        try {
            pass = receiptService.validateVerificationCode(receiptId, registerData.getFeedback());
        } catch (NoReceiptException e) {
            return new ResponseResult().setCode(600).setMessage("没有有效的receipt id");
        }
        if (!pass) {
            return new ResponseResult().setCode(403).setMessage("验证码错误或过期");
        }

        return registerService.register(registerData.getName(), registerData.getPassword(), registerData.getEmail());
    }
}
