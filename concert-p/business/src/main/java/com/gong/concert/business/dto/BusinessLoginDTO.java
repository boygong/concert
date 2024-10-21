package com.gong.concert.business.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author ToastFish
 * @Time 2024/10/21
 */
@Data
public class BusinessLoginDTO {
    @NotBlank(message = "[用户名]不能为空")
    private String userName;
    @NotBlank(message = "[密码]不能为空")
    private String password;
}
