package com.gong.concert.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/12/5
 * @Describe 商家拒绝订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderBatchDTO {
    private List<String> orderId;
    private String concertId;
    private String rejectionReason; //拒绝理由
}
