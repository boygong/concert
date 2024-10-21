package com.gong.concert.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListDTO {
    private String businessId;
    private String name;
    private String username;
    private String password;
    private String phone;
    private Short sex; //性别(1.男  0.女)
    private Short identity; //身份(0.管理员   1.商家)
    private Short status;//状态(0.禁用 1.启用)
    private String idNumber; //身份证
    private String type; //获取类型0为只获取商家，1为只获取管理员，2为全部获取
}
