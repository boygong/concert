package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "business",url = "${open-feign.business.api-url}/business/business")
public interface BusinessClient {
    @GetMapping("/getOne")
    Result getOne(@RequestParam String username);
}
