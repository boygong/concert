package com.gong.concert.business.mapper;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.vo.BusinessVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */

public interface BusinessMapper {

    List<BusinessVO> selectBusinessList(BusinessListDTO dto);
}
