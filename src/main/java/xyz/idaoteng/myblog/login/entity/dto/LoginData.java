package xyz.idaoteng.myblog.login.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginData {
    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String feedback;
}
