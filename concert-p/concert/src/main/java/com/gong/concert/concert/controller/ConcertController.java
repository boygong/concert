package com.gong.concert.concert.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.resp.Result;
import com.gong.concert.concert.dto.AuditConcertDTO;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.dto.SaveConcertDTO;
import com.gong.concert.concert.dto.UpdateConcertDTO;
import com.gong.concert.concert.entity.Concert2;
import com.gong.concert.concert.service.ConcertService;
import com.gong.concert.concert.vo.ConcertVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */

@RestController
@RequestMapping("/concert")
@Slf4j
public class ConcertController {
    @Autowired
    private ConcertService concertService;

    /**
     * @description:新增演唱会接口
     * @author: gongyuankang
     * @date: 2024/11/13 14:39
     * @return: com.gong.concert.common.resp.Result
    */
    @PostMapping("/save")
    public Result save(@RequestBody SaveConcertDTO dto){
        try {
            concertService.saveConcert(dto);
            return Result.success("演唱会新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("新增演唱会失败");
        }
    }
    /**
     * @description: 分页查询演唱会信息(管理员)
     * @author: gongyuankang
     * @date: 2024/11/13 17:23
     * @return: com.gong.concert.common.resp.Result
    */
    @PostMapping("/pageQuery")
   //@Cacheable(value = "concerts", key = "#dto.hashCode().toString()")
    public Result pageQuery(@RequestBody QueryConcertByPageDTO dto) {
        PageResult pageResult = concertService.pageQuery(dto);
        return Result.success(pageResult);
    }

    @PostMapping("/pageQueryUser")
    @SentinelResource(value = "concertPageQuery")
//    @Cacheable(value = "concertCache", key = "#dto.hashCode().toString()")
    public Result pageQueryUser(@RequestBody QueryConcertByPageDTO dto){
        PageResult pageResult = concertService.pageQueryUser(dto);
        return Result.success(pageResult);
    }

    /**
     * @description: 根据演唱会id查询演唱会
     * @author: gongyuankang
     * @date: 2024/11/19 0:25
     * @return: com.gong.concert.common.resp.Result<com.gong.concert.concert.vo.ConcertVO>
    */
    @GetMapping("/getById")
    @Cacheable(value = "concertCache",key = "#concertId")
    @SentinelResource(value = "concertGetById")
    public Result<ConcertVO> getById(@RequestParam String concertId){
        ConcertVO vo = concertService.getById(concertId);
        return Result.success(vo);
    }

    /**
     * @description: 根据演唱会id查询演唱会
     * @author: gongyuankang
     * @date: 2024/11/19 0:25
     * @return: com.gong.concert.common.resp.Result<com.gong.concert.concert.vo.ConcertVO>
     */
    @GetMapping("/getByIdFeign")
    public Concert2 getByIdFeign(@RequestParam String concertId){
        return concertService.getByIdFeign(concertId);
    }
    /**
     * @description: 停售演唱会
     * @author: gongyuankang
     * @date: 2024/11/25 21:20
     * @return: com.gong.concert.common.resp.Result
    */
    @PutMapping("/stop")
    @CacheEvict(value = "concertCache",key = "#concertId")
    public Result stopSale(@RequestParam String concertId){
        int i = concertService.stopSale(concertId);
        return i==1? Result.success():Result.error("停售失败");
    }

    /**
     * @description: 起售演唱会
     * @author: gongyuankang
     * @date: 2024/11/25 22:58
     * @return: com.gong.concert.common.resp.Result
    */
    @PutMapping("/start")
    @CacheEvict(value = "concertCache",key = "#concertId")
    public Result startSale(@RequestParam String concertId){
        int i = concertService.startSale(concertId);
        return i==1? Result.success():Result.error("启售失败");
    }

    @PutMapping("/updateStatus")
    @CacheEvict(value = "concertCache",key = "#concertId")
    public int updateStatus(@RequestParam String concertId,@RequestParam Short status){
        return concertService.updateStatus(concertId,status);
    }

    @PutMapping("/update")
    @CacheEvict(value = "concertCache",key = "#dto.concertId")
    public Result update(@RequestBody UpdateConcertDTO dto){
        concertService.update(dto);
        return Result.success();
    }

    /**
     * @description: 审核演唱hi
     * */
    @PostMapping("/audit")
    @CacheEvict(value = "concertCache",key = "#dto.concertId")
    public Result audit(@RequestBody AuditConcertDTO  dto){
        concertService.audit(dto);
        Result result = new Result<>();
        result.setCode(1);
        result.setMsg("演唱会审核成功");
        return result;
    }
}
