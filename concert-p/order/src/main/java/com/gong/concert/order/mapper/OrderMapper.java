package com.gong.concert.order.mapper;

import com.gong.concert.order.entity.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface OrderMapper {
    int insert(Order orderDb);

    @Select("select * from orders where order_id = #{orderId}")
    Order selectById(String orderId);

    int update(Order orderDb);
}
