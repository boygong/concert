package com.gong.concert.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.gong.concert.common.context.LoginBusinessContext;
import com.gong.concert.common.resp.BusinessLoginVO;
import com.gong.concert.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;

/**
 * @Author ToastFish
 * @Time 2024/10/21
 * 拦截器：Spring框架特有的，常用于登录校验，权限校验，请求日志打印
 */
@Component
@Slf4j
public class BusinessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("BusinessInterceptor开始");
        //获取header的token参数
        String token = request.getHeader("token");
        log.info("token值:{}",token);
        if (StrUtil.isNotBlank(token)) {
            log.info("获取商家登录token：{}", token);
            JSONObject loginBusiness = JwtUtil.getJSONObject(token);
            log.info("当前商家会员：{}", loginBusiness);
            BusinessLoginVO business= JSONUtil.toBean(loginBusiness, BusinessLoginVO.class);
            log.info("存入到线程中的会员:{}",business);
            LoginBusinessContext.setBusiness(business); //存入当前会员对象
        }
        log.info("BusinessInterceptor结束");
        return true;
    }
}
