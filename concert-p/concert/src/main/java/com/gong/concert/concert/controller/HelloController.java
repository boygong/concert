package com.gong.concert.concert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/11/6
 */
@RestController
@RequestMapping
public class HelloController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @GetMapping("/hello")
    public String hello() {
        return "Hello Concert!";
    }

    @GetMapping("/redis")
    public String redis(){
        redisTemplate.opsForValue().set("test", "Hello Redis!");
        String value = redisTemplate.opsForValue().get("test");
        System.out.println(value);  // 应该输出 "Hello Redis!"
        return value;
    }
}
