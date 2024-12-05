package com.gong.concert.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/12/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryVO {
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
}
