package com.gong.concert.order.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.order.dto.*;
import com.gong.concert.order.vo.CreateOrderVO;
import com.gong.concert.order.vo.OrderDetailVO;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface OrderService {
    CreateOrderVO createOrder(CreateOrderDTO dto);

    String confirm(ConfirmOrderDTO dto);

    PageResult pageQuery(OrderPageQueryDTO dto);

    void cancelOrder(CancelOrderDTO dto);

    void rejectOrder(RejectOrderDTO dto);

    OrderDetailVO detail(String orderId);
}
