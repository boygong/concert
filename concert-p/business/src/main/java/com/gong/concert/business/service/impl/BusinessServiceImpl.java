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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<BusinessVO> getList(BusinessListDTO getBusinessList) {
        log.trace("调用映射接口:getBusinessList");
        try {
            List<BusinessVO> businessVOS = businessMapper.selectBusinessList(getBusinessList);
            log.trace("映射出参:{}",businessMapper.selectBusinessList(getBusinessList));
            log.trace("出参：{}",businessVOS);
            return businessVOS;
        }catch (Exception e){
            log.error("调用失败,返回",e);
        }
        return null;
    }

    @Override
    public BusinessLoginVO login(BusinessLoginDTO loginDTO) {
        String userName = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        log.info("参数校验:{},{}",userName,password);
        Business business = new Business();
        business.setUsername(userName);
        business.setPassword(password);
        Business businessDB = businessMapper.selectByExample(business);
        log.info("调用BusinessMapper映射selectByExample出参:{}",businessDB);
        if (businessDB == null){
            log.error("查找到的商户数为空");
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_NOT_EXIST);
        }
        if (!businessDB.getPassword().equals(password) || !businessDB.getUsername().equals(userName)){
            log.error("输入账户密码和实际账户密码:{},{},{},{}",userName,password,businessDB.getUsername(),businessDB.getPassword());
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_IS_WARN);
        }
        BusinessLoginVO businessLoginVO = BeanUtil.copyProperties(businessDB, BusinessLoginVO.class);
        String token = JwtUtil.createToken(businessLoginVO.getBusinessId(), businessLoginVO.getPhone());
        businessLoginVO.setToken(token);
        return businessLoginVO;
    }
}
