package com.gong.concert.users.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.users.dto.QueryUserDTO;
import com.gong.concert.users.dto.SaveUserDTO;
import com.gong.concert.users.dto.UserLoginDTO;
import com.gong.concert.users.entity.User;
import com.gong.concert.users.vo.UserLoginVO;

/**
 * @Author ToastFish
 * @Time 2024/10/23
 */
public interface UserService {
    User getByUserName(String username);

    PageResult pageQuery(QueryUserDTO dto);

    int save(SaveUserDTO dto);

    UserLoginVO login(UserLoginDTO dto);
}
