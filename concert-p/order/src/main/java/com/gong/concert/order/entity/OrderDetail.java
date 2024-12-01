package com.gong.concert.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/12/1
 * 订单明细
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private String orderDetailId;
    private String name;
    private String image;
    private String orderId;
    private String seatId;
    private Double amount;
    private Integer row;
    private Integer col;
}
