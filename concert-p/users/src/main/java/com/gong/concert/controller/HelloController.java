package com.gong.concert.controller;

import com.gong.concert.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/10/22
 * 用户控制层测试
 */

@RestController
@Slf4j
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public Result hello(){
        return Result.success("Hello Users!");
    }
}
