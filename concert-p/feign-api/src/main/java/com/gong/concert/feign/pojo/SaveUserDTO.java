package com.gong.concert.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ToastFish
 * @Time 2024/11/5
 * 前端传入保存用户对象实体类
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveUserDTO {
    private String name;
    private String username;
    private String password;
    private String avatar;
    private String email;
    private String idNumber;
    private Short sex;
    private String phone;
}
