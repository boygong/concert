package com.gong.concert.common.exception;

import lombok.AllArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/5/6
 */


@AllArgsConstructor
public enum BusinessExceptionEnum {
    BUSINESS_IS_NULL("查询商家为空"),
    BUSINESS_NOT_EXIST("商家不存在"),
    PASSWORD_IS_WARN("账户或密码错误"),
    BUSINESS_IS_EXIST("该账户已注册"),
    PHONE_IS_ERROR("手机号码错误"),
    IDNUMBER_IS_ERROR("身份证错误");
//    MEMBER_MOBILE_EXIST("手机号已注册"),
//    MEMBER_MOBILE_NOT_EXIST("请先获取短信验证码"),
//    MEMBER_MOBILE_CODE_ERROR("短信验证码错误");

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private String desc;
}
