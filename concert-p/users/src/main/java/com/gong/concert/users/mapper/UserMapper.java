package com.gong.concert.users.mapper;

import com.gong.concert.users.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ToastFish
 * @Time 2024/10/23
 * 用户映射层
 */
public interface UserMapper {
    @Select("select * from  user u where u.username = #{username}")
    User selectByUserName(String username);
}
