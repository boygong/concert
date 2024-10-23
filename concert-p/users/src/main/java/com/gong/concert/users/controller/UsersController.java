package com.gong.concert.users.controller;

import com.gong.concert.common.resp.Result;
import com.gong.concert.users.entity.User;
import com.gong.concert.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/10/23
 * 用户控制层
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserService userService;
    @GetMapping("/{username}")
    public Result queryByUserName(@PathVariable String username){
        log.info("控制层入参:{}",username);
        User user = userService.getByUserName(username);
        return Result.success(user);
    }
}
