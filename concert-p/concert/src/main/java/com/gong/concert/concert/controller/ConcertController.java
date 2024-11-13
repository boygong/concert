package com.gong.concert.concert.controller;

import com.gong.concert.common.resp.Result;
import com.gong.concert.concert.dto.SaveConcertDTO;
import com.gong.concert.concert.service.ConcertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
