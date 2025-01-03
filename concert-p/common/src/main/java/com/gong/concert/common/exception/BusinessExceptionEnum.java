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
    IDNUMBER_IS_ERROR("身份证错误"),
    BUSINESS_IS_BAN("商家被停用"),
    BUSINESS_UPDATE_ERROR("商家信息更新错误"),
    Not_MODIFY_YOURSELF("不能修改自己"),
    USERNAME_IS_NULL("用户名为空"),
    UPDATE_SEAT_FAIL("更新座位失败"),
    UPDATE_CONCERT_FAIL("更新演唱会信息失败"),
    USERID_IS_NULL("用户id为空"),
    CONCERTID_IS_NULL("演唱会id为空"),
    SEATNUM_IS_NULL("座位数为空"),
    SEATNUM_IS_ERROR("座位数不匹配");
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
