package com.gong.concert.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/11/30
 * 订单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private String userId;
    private String concertId;
    private String addressBookId;
    private Short payStatus;
    private Short payMethod;
    private Double amount;
    private String remark;
    private String phone;
    private String address;
    private String userName;
    private String consignee;
    private String cancelReason;
    private String rejectionReason;
    private LocalDateTime cancelTime;
    private LocalDateTime beginTime;
    private Short orderStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
