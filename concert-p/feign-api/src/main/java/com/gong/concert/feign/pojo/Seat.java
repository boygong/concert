package com.gong.concert.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 座位实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Integer id;//数据库id，自增
    private String seatId;//座位id
    private String concertId; //关联演唱会id
    private Double fee;//座位费用
    private Short seatStatus;//座位状态0启用 1停用 2维修 5待支付 6售出
    private Short seatType;//座位等级
    private Integer seatRow;//座位所处展厅的行
    private Integer seatCol;//座位所处展厅的列
}
