package com.gong.concert.order.controller;

import com.gong.concert.common.resp.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public Result hello(){
        return Result.success("Hello Order");
    }
}
