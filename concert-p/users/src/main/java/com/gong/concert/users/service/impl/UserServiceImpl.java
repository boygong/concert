package com.gong.concert.users.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gong.concert.common.context.LoginBusinessContext;
import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.common.exception.OrderException;
import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.util.JwtUtil;
import com.gong.concert.common.util.SnowUtil;
import com.gong.concert.users.dto.QueryUserDTO;
import com.gong.concert.users.dto.SaveUserDTO;
import com.gong.concert.users.dto.UserLoginDTO;
import com.gong.concert.users.entity.User;
import com.gong.concert.users.mapper.UserMapper;
import com.gong.concert.users.service.UserService;
import com.gong.concert.users.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public PageResult pageQuery(QueryUserDTO dto) {
        log.info("用户分页查询进入Service层:{},{}",dto.getPage(),dto.getSize());
        //开始分页查询
        PageHelper.startPage(dto.getPage(), dto.getSize());
        Page<User> page = userMapper.pageQuery(dto);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public int save(SaveUserDTO dto) {
        User userDb = new User();
        BeanUtil.copyProperties(dto,userDb);
        userDb.setCreateTime(LocalDateTime.now());
        userDb.setUserId(SnowUtil.getSnowflakeNextIdStr());
        if (dto.getUsername() == null || dto.getUsername().equals("")){
            throw new BusinessException(BusinessExceptionEnum.USERNAME_IS_NULL);
        }
        int i = userMapper.save(userDb);
        return i;
    }

    @Override
    public UserLoginVO login(UserLoginDTO dto) {
        log.info("商家登录Service层");
        String userName = dto.getUsername();
        String password = dto.getPassword();
        log.info("参数校验:{},{}", userName, password);
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        User userDb = userMapper.selectByExample(user);
        log.info("调用UserMapper映射selectByExample出参:{}", userDb);
        if (userDb == null) {
            log.error("查找到的用户数为空");
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_WARN);
        }
        if (!userDb.getPassword().equals(password) || !userDb.getUsername().equals(userName)) {
            log.error("输入账户密码和实际账户密码:{},{},{},{}", userName, password, userDb.getUsername(), userDb.getPassword());
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_WARN);
        }
        UserLoginVO vo = new UserLoginVO();
        BeanUtil.copyProperties(userDb, vo);
        try {
            String token = JwtUtil.createToken(userDb.getUsername(), userDb.getPhone());
            vo.setToken(token);
        }catch (Exception e){
            throw new OrderException("创建token异常");
        }
        return vo;
    }
}
