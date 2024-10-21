package com.gong.concert.business.service;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.dto.BusinessLoginDTO;
import com.gong.concert.business.entity.Business;
import com.gong.concert.business.vo.BusinessLoginVO;
import com.gong.concert.business.vo.BusinessVO;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */

public interface BusinessService {
    List<BusinessVO> getList(BusinessListDTO getBusinessList);

    BusinessLoginVO login(BusinessLoginDTO loginDTO);

    void save(Business business);
}
