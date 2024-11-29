package com.gong.concert.order.controller;

import com.gong.concert.common.resp.Result;
import com.gong.concert.order.dto.CreateOrderDTO;
import com.gong.concert.order.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * @description: 创建演唱会订单
     * @author: gongyuankang
     * @date: 2024/11/29 18:26
     * @return: com.gong.concert.common.resp.Result
    */
    @PostMapping("/createOrder")
    public Result createOrder(@RequestBody CreateOrderDTO dto){
        boolean flag = orderService.createOrder(dto);
        return Result.success();
    }
}
