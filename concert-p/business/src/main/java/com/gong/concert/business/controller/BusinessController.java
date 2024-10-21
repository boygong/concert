package com.gong.concert.business.controller;

import com.gong.concert.business.dto.BusinessListDTO;
import com.gong.concert.business.dto.BusinessLoginDTO;
import com.gong.concert.business.entity.Business;
import com.gong.concert.business.service.BusinessService;
import com.gong.concert.business.vo.BusinessLoginVO;
import com.gong.concert.business.vo.BusinessVO;
import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gong.concert.common.resp.Result;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 * @Descripe 商家控制层
 */
@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {
    @Autowired
    private BusinessService businessService;
    @PostMapping("/getList")
    public Result getBusinessList(@RequestBody BusinessListDTO getBusinessList){
        List<BusinessVO>  list = businessService.getList(getBusinessList);
        if (list == null || list.size() == 0){
            log.info("枚举值:{}",BusinessExceptionEnum.BUSINESS_IS_NULL.getDesc());
            BusinessException businessException = new BusinessException(BusinessExceptionEnum.BUSINESS_IS_NULL);
            System.out.println(businessException);
//            throw new BusinessException(BusinessExceptionEnum.BUSINESS_IS_NULL);
            return null;
        }else {
            return Result.success(list);
        }
    }

    @PostMapping("/login")
    public Result login(@Valid @RequestBody BusinessLoginDTO loginDTO){
        BusinessLoginVO businessLoginVO = businessService.login(loginDTO);
        return Result.success(businessLoginVO);
    }
}
