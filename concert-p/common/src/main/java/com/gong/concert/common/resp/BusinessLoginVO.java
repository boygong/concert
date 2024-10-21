package com.gong.concert.common.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/10/21
 */
@Data
public class BusinessLoginVO {
    private String businessId;
    private String name;
    private String username;
    private String password;
    private String phone;
    private short sex; //性别(1.男  0.女)
    private short identity; //身份(0.管理员   1.商家)
    private short status;//状态(0.禁用 1.启用)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;
    private String idNumber; //身份证

    private String token;
}
