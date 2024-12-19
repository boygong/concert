package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.Concert2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author ToastFish
 * @Time 2024/12/3
 */
@FeignClient(name = "users",url = "${open-feign.concert.api-url}/concert/concert")
public interface ConcertClient {
    @GetMapping("/getByIdFeign")
    public Concert2 getByIdFeign(@RequestParam String concertId);

    @PutMapping("/updateStatus")
    public int updateStatus(@RequestParam String concertId,@RequestParam Short status);
}
