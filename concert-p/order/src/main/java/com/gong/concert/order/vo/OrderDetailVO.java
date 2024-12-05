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
 * @Time 2024/12/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVO {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updateTime;

    private Integer detailNum; //订单明细数
    List<OrderDetail> orderDetails;
}
