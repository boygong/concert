package com.gong.concert.common.exception;

import lombok.AllArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/5/6
 */


@AllArgsConstructor
public enum BusinessExceptionEnum {
    BUSINESS_IS_NULL("查询商家为空");
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
