package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.SaveUserDTO;
import com.gong.concert.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ToastFish
 * @Time 2024/10/22
 */

@FeignClient(name = "users",url = "${open-feign.user.api-url}/users/users")
public interface UserClient {
    @GetMapping("/{username}")
    User findByUserName(@PathVariable("username") String username);

    @PostMapping("/save")
    int saveUser(@RequestBody SaveUserDTO dto);
}
