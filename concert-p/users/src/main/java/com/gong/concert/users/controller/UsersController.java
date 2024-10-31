package com.gong.concert.users.controller;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.resp.Result;
import com.gong.concert.users.dto.QueryUserDTO;
import com.gong.concert.users.entity.User;
import com.gong.concert.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public User queryByUserName(@PathVariable String username){
        log.info("控制层入参:{}",username);
        User user = userService.getByUserName(username);
        return user;
    }

    @PostMapping("/pageQuery")
    public Result<PageResult> queryUserPage(@RequestBody QueryUserDTO dto){
        log.info("进入分页查询控制层:{}",dto);
        PageResult pageResult = userService.pageQuery(dto);
        return Result.success(pageResult);
    }
}
