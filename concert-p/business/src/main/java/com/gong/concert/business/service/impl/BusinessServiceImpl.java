package com.gong.concert.business.service.impl;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.mapper.BusinessMapper;
import com.gong.concert.business.service.BusinessService;
import com.gong.concert.business.vo.BusinessVO;
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
}
