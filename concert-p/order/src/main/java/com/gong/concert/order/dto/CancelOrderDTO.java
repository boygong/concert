package com.gong.concert.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/12/5
 * （用户）取消订单接口入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderDTO {
    private String orderId;
    private String cancelReason;
}
