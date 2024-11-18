package com.gong.concert.concert.mapper;

import com.gong.concert.concert.entity.Seat;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 座位映射层
 */
public interface SeatMapper {
    int insertSeat(Seat seat);

    @Select("select * from seats where concert_id = #{concertId}")
    List<Seat> getByConcertId(String concertId);
}
