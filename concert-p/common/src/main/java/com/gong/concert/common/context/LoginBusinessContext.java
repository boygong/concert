package com.gong.concert.common.context;


import com.gong.concert.common.resp.BusinessLoginVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author ToastFish
 * @Time 2024/10/21
 */

@Slf4j
public class LoginBusinessContext {
    private static ThreadLocal<BusinessLoginVO> business = new ThreadLocal<>();
    public static BusinessLoginVO getBusiness() {
        return business.get();
    }

    public static void setBusiness(BusinessLoginVO business) {
        LoginBusinessContext.business.set(business);
    }

    public static String getUsername() {
        try {
            return business.get().getUsername();
        } catch (Exception e) {
            log.error("获取登录会员信息异常", e);
            throw e;
        }
    }
}
