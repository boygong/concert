package com.gong.concert.concert.controller;

import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.service.SeatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getByNum")
    public List<Seat> getByNum(@RequestParam String concertId ,@RequestParam Integer seatNum){
        return seatService.getByNum(concertId,seatNum);
    }

    @PutMapping("/updateStatus")
    boolean updateStatus(@RequestBody Seat seat, @RequestParam  Short status){
        return seatService.updateStatus(seat,status);
    }
}
