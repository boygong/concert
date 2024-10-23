package com.gong.concert.users.service.impl;

import com.gong.concert.users.entity.User;
import com.gong.concert.users.mapper.UserMapper;
import com.gong.concert.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ToastFish
 * @Time 2024/10/23
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getByUserName(String username) {
        User user = userMapper.selectByUserName(username);
        log.info("调用映射出参:{}",user);
        return user;
    }
}
