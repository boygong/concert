package com.gong.concert.concert.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CacheLoggingAspect {


    // 拦截 @Cacheable 注解的方法，执行前打印日志
    @Before("@annotation(cacheable)")
    public void logBeforeCache(Cacheable cacheable) {
        // 打印缓存查询前的日志
        log.info("Checking cache for: {}", cacheable.value());
    }

    // 方法执行完成后，如果有缓存命中，则打印日志
    @AfterReturning(value = "@annotation(cacheable)", returning = "result")
    public void logAfterCache(Cacheable cacheable, Object result) {
        if (result != null) {
            log.info("缓存命中: {}", cacheable.value());
        } else {
            log.info("缓存未命中: {}", cacheable.value());
        }
    }
}
