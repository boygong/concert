package com.gong.concert.business.service;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.vo.BusinessVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */

public interface BusinessService {
    List<BusinessVO> getList(BusinessListDTO getBusinessList);
}
