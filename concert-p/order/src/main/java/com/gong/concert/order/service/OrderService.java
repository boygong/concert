package com.gong.concert.order.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.order.dto.ConfirmOrderDTO;
import com.gong.concert.order.dto.CreateOrderDTO;
import com.gong.concert.order.dto.OrderPageQueryDTO;
import com.gong.concert.order.vo.CreateOrderVO;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface OrderService {
    CreateOrderVO createOrder(CreateOrderDTO dto);

    String confirm(ConfirmOrderDTO dto);

    PageResult pageQuery(OrderPageQueryDTO dto);
}
