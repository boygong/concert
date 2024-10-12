package com.gong.concert.business.vo;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 * @Descripe 商家返回实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessVO {
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
}

