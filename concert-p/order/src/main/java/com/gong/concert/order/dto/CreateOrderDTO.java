package com.gong.concert.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 * @describe 创建订单入参实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private String userId;   //用户id
    private String concertId;  //演唱会id
    private Short isSelected; //是否选号
    private List<String> seatIdList; //座位id列表,注：座位可多选
    private Integer seatNum; //购买座位数
    private String addressBookId; //收货地址id
    private String remark; //用户备注
}
