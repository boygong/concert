package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.Seat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@FeignClient(name = "users",url = "${open-feign.seat.api-url}/concert/seat")
public interface SeatClient {
    @GetMapping("/getById")
    Seat getById(@RequestParam String seatId);

    @GetMapping("/getByNum")
    List<Seat> getByNum(@RequestParam String concertId ,@RequestParam Integer seatNum);

    @PutMapping("/updateStatus")
    boolean updateStatus(Seat seat, Short status);
}
