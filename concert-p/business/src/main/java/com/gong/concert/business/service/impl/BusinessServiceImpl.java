package com.gong.concert.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.dto.BusinessLoginDTO;
import com.gong.concert.business.entity.Business;
import com.gong.concert.business.mapper.BusinessMapper;
import com.gong.concert.business.service.BusinessService;
import com.gong.concert.business.vo.BusinessLoginVO;
import com.gong.concert.business.vo.BusinessVO;
import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.common.util.JwtUtil;
import com.gong.concert.common.util.SnowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public List<BusinessVO> getList(BusinessListDTO dto) {
        log.info("获取商家列表Service层");
        List<BusinessVO> resp = new ArrayList<>();
        log.trace("调用映射接口:getBusinessList");
        try {
            List<Business> businessList = businessMapper.selectListByExample(dto);
            log.trace("映射出参:{}", businessMapper.selectListByExample(dto));
            log.trace("出参：{}", businessList);
            businessList.stream().forEach(business -> {
                BusinessVO businessVO = new BusinessVO();
                BeanUtil.copyProperties(business, businessVO); //拷贝商家类
                resp.add(businessVO);
            });
            return resp;
        } catch (Exception e) {
            log.error("调用失败,返回", e);
        }
        return null;
    }

    @Override
    public BusinessLoginVO login(BusinessLoginDTO loginDTO) {
        log.info("商家登录Service层");
        String userName = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        log.info("参数校验:{},{}", userName, password);
        Business business = new Business();
        business.setUsername(userName);
        business.setPassword(password);
        Business businessDB = businessMapper.selectByExample(business);
        log.info("调用BusinessMapper映射selectByExample出参:{}", businessDB);
        if (businessDB == null) {
            log.error("查找到的商户数为空");
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_WARN);
        }
        if (businessDB.getStatus() == 0) {
            log.info("商家为停用状态");
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_IS_BAN);
        }
        if (!businessDB.getPassword().equals(password) || !businessDB.getUsername().equals(userName)) {
            log.error("输入账户密码和实际账户密码:{},{},{},{}", userName, password, businessDB.getUsername(), businessDB.getPassword());
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_WARN);
        }
        BusinessLoginVO businessLoginVO = BeanUtil.copyProperties(businessDB, BusinessLoginVO.class);
        String token = JwtUtil.createToken(businessLoginVO.getUsername(), businessLoginVO.getPhone());
        businessLoginVO.setToken(token);
        return businessLoginVO;
    }

    @Override
    public void save(Business business) {
        log.info("添加商家Service层，开始检测数据合法性");
        String username = business.getUsername();
        Business toDB = new Business();
        toDB.setUsername(username);
        Business businessDB = businessMapper.selectByExample(toDB); //通过用户名获取商家
        if (businessDB != null) {
            //商家存在抛出异常
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_IS_EXIST);
        }
        if (business.getPhone().length() != 11) {
            //手机号格式错误
            throw new BusinessException(BusinessExceptionEnum.PHONE_IS_ERROR);
        }
        if (business.getIdNumber().length() != 18) {
            //身份证格式错误
            throw new BusinessException(BusinessExceptionEnum.IDNUMBER_IS_ERROR);
        }

        business.setBusinessId(SnowUtil.getSnowflakeNextIdStr());
        business.setStatus((short) 1);
        business.setCreateTime(LocalDateTime.now());
//        business.setCreateUser(LoginBusinessContext.getId());
        business.setCreateUser("admin");


        businessMapper.insertBusiness(business);
    }

    @Override
    public BusinessVO getOne(String username) {
        log.info("商家回显Service层");
        Business toDB = new Business();
        toDB.setUsername(username);
        Business businessDB = businessMapper.selectByExample(toDB); //通过用户名获取商家
        BusinessVO businessVO = BeanUtil.copyProperties(businessDB, BusinessVO.class);
        return businessVO;
    }

    @Override
    public void update(Business business) {
        log.info("商家修改Service层,开始数据校验");
        String username = business.getUsername();
//        if (username.equals(LoginBusinessContext.getUsername())) {
//            throw new BusinessException(BusinessExceptionEnum.Not_MODIFY_YOURSELF);
//        }
        Business toDB = new Business();
        toDB.setUsername(username);
        Business businessDB = businessMapper.selectByExample(toDB); //通过用户名获取商家
        if (businessDB == null) {
            //商家存在抛出异常
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_NOT_EXIST);
        }
        if (business.getPhone() != null) {
            if (business.getPhone().length() != 11) {
                //手机号格式错误
                throw new BusinessException(BusinessExceptionEnum.PHONE_IS_ERROR);
            }
        }
        if (business.getIdNumber() != null && businessDB.getIdNumber() != null && business.getIdNumber().length() != 18) {
            //身份证格式错误
            throw new BusinessException(BusinessExceptionEnum.IDNUMBER_IS_ERROR);
        }

        business.setUpdateTime(LocalDateTime.now());
        business.setUpdateUser("admin"); //先写死
//        business.setUpdateUser(LoginBusinessContext.getUsername());
        int i = businessMapper.updateBusiness(business);
        if (i != 1) {
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_UPDATE_ERROR);
        }
    }
}
