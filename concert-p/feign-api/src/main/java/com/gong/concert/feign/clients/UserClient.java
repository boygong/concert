package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author ToastFish
 * @Time 2024/10/22
 */

@FeignClient("users")
public interface UserClient {
    @GetMapping("/users/{username}")
    User findByUserName(@PathVariable("username") String username);
}
