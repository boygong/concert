package com.gong.concert.business.mapper;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.entity.Business;
import com.gong.concert.business.vo.BusinessVO;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */

public interface BusinessMapper {

    List<BusinessVO> selectBusinessList(BusinessListDTO dto);

    Business selectByExample(Business business);

    List<Business> selectListByExample(BusinessListDTO businessDTO);
}
