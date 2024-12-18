package com.gong.concert.order.dto;

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
public class OrderPageQueryDTO {
    private String orderId;
    private String userId;
    private String concertId;
    private String userName;
    private String phone;
    private LocalDateTime startTime;  //开始时间
    private LocalDateTime endTime;    //结束时间
    private Short payStatus;
    private Short payMethod;
    private Short orderStatus;
    private Double lowAmount;   //最低价
    private Double highAmount;   //最高价
    private String createUser; //创建演唱会商家

    private Integer page;
    private Integer size;
}
