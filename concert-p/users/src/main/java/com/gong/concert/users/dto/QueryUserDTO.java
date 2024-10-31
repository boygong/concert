package com.gong.concert.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/10/31
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserDTO {
    private String userId; //用户id
    private String name;  //用户姓名
    private String username;  //用户登录名
    private String idNumber;  //用户身份证
    private Short sex; //性别(1为男，0为女)
    private LocalDateTime createTime;
    private String phone;  //手机号

    private Integer page; //页码
    private Integer size; //每页条数
}
