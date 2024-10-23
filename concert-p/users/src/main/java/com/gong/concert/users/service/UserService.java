package com.gong.concert.users.service;

import com.gong.concert.users.entity.User;

/**
 * @Author ToastFish
 * @Time 2024/10/23
 */
public interface UserService {
    User getByUserName(String username);
}
