package com.gong.concert.common.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLockUtil {

    @Autowired
    private  RedisTemplate<String, String> redisTemplate;

    /**
     * 获取分布式锁
     * @param lockKey 锁的唯一标识
     * @param timeout 锁的超时时间，单位：秒
     * @param maxWaitTime 最大等待时间，单位：秒
     * @param retryInterval 重试间隔时间，单位：秒
     * @return true：成功获得锁，false：未能获得锁
     */
    public boolean lock(String lockKey, long timeout, long maxWaitTime, long retryInterval) {
        int count = 0;
        long startTime = System.currentTimeMillis();
        log.info("进入加锁开始加锁:{}",lockKey);
        while (System.currentTimeMillis() - startTime < maxWaitTime * 1000) {
            // 使用SETNX命令设置锁，如果锁不存在则成功，存在则失败
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCK", timeout, TimeUnit.SECONDS);

            if (lockAcquired != null && lockAcquired) {
                return true;  // 获取锁成功
            }

            // 如果锁未成功获取，等待一段时间后重试
            try {
                log.info("进入加锁失败重试:{}",++count);
                Thread.sleep(retryInterval * 1000);  // 等待重试
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;  // 超过最大等待时间仍未获取到锁
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁的唯一标识
     */
    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}
