package com.gong.concert.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/10/10
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello World! Business";
    }
}
