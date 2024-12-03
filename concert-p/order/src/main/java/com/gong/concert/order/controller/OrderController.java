package com.gong.concert.order.controller;

import com.gong.concert.common.resp.Result;
import com.gong.concert.order.dto.ConfirmOrderDTO;
import com.gong.concert.order.dto.CreateOrderDTO;

import com.gong.concert.order.service.OrderService;
import com.gong.concert.order.vo.CreateOrderVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


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
        CreateOrderVO vo = orderService.createOrder(dto);
        return Result.success(vo);
    }
    /**
     * @description:确认订单接口
     * @author: gongyuankang
     * @date: 2024/12/3 22:09
     * @return: com.gong.concert.common.resp.Result
    */
    @PutMapping("/confirmOrder")
    public Result confirmOrder(@RequestBody ConfirmOrderDTO dto){
        String confirm = orderService.confirm(dto);
        return Result.success(confirm);
    }
}
