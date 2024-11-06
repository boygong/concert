package com.gong.concert.concert.controller;

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
    @GetMapping("/hello")
    public String hello(){
       return "Hello Concert!";
    }
}
