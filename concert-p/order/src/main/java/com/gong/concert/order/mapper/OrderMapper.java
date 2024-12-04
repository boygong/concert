package com.gong.concert.order.mapper;

import com.github.pagehelper.Page;
import com.gong.concert.order.dto.OrderPageQueryDTO;
import com.gong.concert.order.entity.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface OrderMapper {
    int insert(Order orderDb);

    @Select("select * from orders where order_id = #{orderId}")
    Order selectById(String orderId);

    int update(Order orderDb);

    Page<Order> select(OrderPageQueryDTO dto);
}
