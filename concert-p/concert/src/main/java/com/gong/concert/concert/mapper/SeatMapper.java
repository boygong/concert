package com.gong.concert.concert.mapper;

import com.gong.concert.concert.entity.Seat;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Update("update seats set seat_status = #{seatStatus} where concert_id = #{concertId} and seat_id = #{seatId}")
    int updateStatus(String concertId, String seatId, Short seatStatus);
}
