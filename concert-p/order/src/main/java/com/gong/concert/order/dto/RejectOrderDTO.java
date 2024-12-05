package com.gong.concert.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/12/5
 * @Describe 商家拒绝订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderDTO {
    private String orderId;
    private String rejectionReason; //拒绝理由
}
