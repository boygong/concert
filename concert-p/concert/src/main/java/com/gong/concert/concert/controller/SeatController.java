package com.gong.concert.concert.controller;

import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.service.SeatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@RestController
@RequestMapping("/seat")
public class SeatController {
    @Resource
    private SeatService seatService;
    @GetMapping("/getById")
    public Seat getById(@RequestParam String seatId){
        Seat seat = seatService.getById(seatId);
        return seat;
    }
}
