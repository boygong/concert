package com.gong.concert.concert.mapper;

import com.gong.concert.concert.entity.Seat;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update seats set seat_status = #{seatStatus} where seat_id = #{seatId}")
    int updateStatus(String seatId, Short seatStatus);

    @Select("select * from seats where seat_id = #{seatId}")
    Seat getBySeatId(String seatId);

    @Select("select * from seats where concert_id=#{concertId} and seat_status = 0 limit 0,#{num}")
    List<Seat> getByNum(String concertId,Integer num);

    @Select("select count(*) from seats where concert_id =#{concertId} and seat_status = #{status}")
    int selectCountByStatus(String concertId, short status);

    @Select("select count(*) from seats where concert_id =#{concertId}")
    int selectCount(String concertId);
}
