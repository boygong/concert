package com.gong.concert.order.service;

import com.gong.concert.order.dto.CreateOrderDTO;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface OrderService {
    boolean createOrder(CreateOrderDTO dto);
}
