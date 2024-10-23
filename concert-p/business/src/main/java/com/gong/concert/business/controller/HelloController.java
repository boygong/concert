package com.gong.concert.business.controller;

import com.gong.concert.feign.clients.UserClient;
import com.gong.concert.feign.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/10/10
 */
@RestController
public class HelloController {
    @Autowired
    private UserClient userClient;
    @GetMapping("/hello")
    public String hello(){
        return "Hello World! Business";
    }

    @GetMapping("/user")
    public User testFeign(){
        User user = userClient.findByUserName("user1");
        return user;
    }
}
