package com.gong.concert.users.controller;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.resp.Result;
import com.gong.concert.users.dto.QueryUserDTO;
import com.gong.concert.users.dto.SaveUserDTO;
import com.gong.concert.users.dto.UserLoginDTO;
import com.gong.concert.users.entity.User;
import com.gong.concert.users.service.UserService;
import com.gong.concert.users.vo.UserLoginVO;
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
    public Result queryByUserName(@PathVariable String username){
        log.info("控制层入参:{}",username);
        User user = userService.getByUserName(username);
        return Result.success(user);
    }

    @PostMapping("/pageQuery")
    public Result<PageResult> queryUserPage(@RequestBody QueryUserDTO dto){
        log.info("进入分页查询控制层:{}",dto);
        PageResult pageResult = userService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @PostMapping("/save")
    public int save(@RequestBody SaveUserDTO dto){
        log.info("进入分页查询控制层:{}",dto);
        int flag = userService.save(dto);
        return flag;
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO dto){
        log.info("用户登录控制层:{}",dto);
        UserLoginVO vo = userService.login(dto);
        return Result.success(vo);
    }
}
