package com.gong.concert.feign.pojo;

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
    private Short inUrl;  //进入的服务不为null一律看做为feign调用
    private List<String> orderId;
    private String concertId;
    private String rejectionReason; //拒绝理由
}
