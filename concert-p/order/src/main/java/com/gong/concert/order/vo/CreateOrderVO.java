package com.gong.concert.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gong.concert.order.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/12/3
 * @Describe 创建订单返回结果
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderVO {
    private String orderId; //订单号
    private String userName;//用户名
    private String concertName; //演唱会ming
    private Double amount;//订单总金额
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime beginTime;//演出开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime; //订单创建时间
    private String image; //演唱会图片
    private List<OrderDetail> orderDetails; //订单明细信息
}
