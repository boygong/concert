package com.gong.concert.business.controller;

import com.gong.concert.common.resp.Result;
import com.gong.concert.feign.clients.UserClient;
import com.gong.concert.feign.pojo.SaveUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/11/5
 * 管理员操作用户信息控制层
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserClient userClient;
    @PostMapping("/save")
    public Result save(@RequestBody SaveUserDTO dto){
        int i = userClient.saveUser(dto);
        log.info("调用新增用户服务出参:{}",i);
        if (i==1){
            return Result.success();
        }else {
            return Result.error("新增用户失败");
        }
    }
}
