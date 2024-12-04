package com.gong.concert.order.mapper;

import com.gong.concert.order.entity.OrderDetail;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ToastFish
 * @Time 2024/12/1
 */
public interface OrderDetailMapper {

    int insert(OrderDetail detail);

    @Select("select count(*) from order_detail where order_id=#{orderId}")
    int selectCountByOrderId(String orderId);
}
