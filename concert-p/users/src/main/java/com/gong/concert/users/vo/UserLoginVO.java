package com.gong.concert.users.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {
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

    private String token;
}
