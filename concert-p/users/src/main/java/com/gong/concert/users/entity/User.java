package com.gong.concert.users.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/10/22
 * @Descripe 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String name;
    private String username;
    private String password;
    private String avatar;
    private String email;
    private String idNumber;
    private Short sex; //性别(1为男，0为女)
    private LocalDateTime createTime;
    private String phone;
}
