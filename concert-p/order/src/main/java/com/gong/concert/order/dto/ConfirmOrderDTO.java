package com.gong.concert.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/12/3
 * 确认订单接口
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderDTO {
    private String orderId;  //订单状态
    private Short payMethod; //0 微信 1支付宝 2其他
    private Double amount; //支付金额
}
