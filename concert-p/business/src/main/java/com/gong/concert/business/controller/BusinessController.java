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
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    /**
     * @description: 查询商家列表
     * @author: gongyuankang
     * @date: 2024/10/21 17:18
     * @return: Result
    */
    @PostMapping("/getList")
    public Result getBusinessList(@RequestBody BusinessListDTO getBusinessList){
        log.info("入参实体类getBusinessList:{}",getBusinessList);
        List<BusinessVO>  list = businessService.getList(getBusinessList);
        if (list == null || list.size() == 0){
            BusinessException businessException = new BusinessException(BusinessExceptionEnum.BUSINESS_IS_NULL);
            System.out.println(businessException);
//            throw new BusinessException(BusinessExceptionEnum.BUSINESS_IS_NULL);
            return null;
        }else {
            return Result.success(list);
        }
    }

    /**
     * @description:  商家登录接口
     * @author: gongyuankang
     * @date: 2024/10/21 17:17
     * @return: Result<BusinessLoginVO>
    */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody BusinessLoginDTO loginDTO){
        BusinessLoginVO businessLoginVO = businessService.login(loginDTO);
        return Result.success(businessLoginVO);
    }

    /**
     * @description: 添加商家
     * @author: gongyuankang
     * @date: 2024/10/22 9:49
     * @return:
    */
    @PostMapping("/save")
    public Result save(@Valid @RequestBody Business business){
        businessService.save(business);
        return Result.success();
    }

    /**
     * @description: 商家回显接口
     * @author: gongyuankang
     * @date: 2024/10/22 9:50
     * @return:
    */
    @GetMapping("/getOne")
    public Result getOne(@RequestParam String username){
        BusinessVO businessVO = businessService.getOne(username);
        return Result.success(businessVO);
    }

    /**
     * @description: 商家信息修改
     * @author: gongyuankang
     * @date: 2024/10/22 9:58
     * @return:
    */
    @PutMapping("/update")
    public Result update(@RequestBody Business business){
        businessService.update(business);
        return Result.success();
    }
}
