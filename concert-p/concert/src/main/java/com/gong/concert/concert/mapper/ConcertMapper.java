package com.gong.concert.concert.mapper;

import com.github.pagehelper.Page;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.entity.Concert;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */
public interface ConcertMapper {
    int insertConcert(Concert concert);

    Page<Concert> pageQuery(QueryConcertByPageDTO dto);

    @Update("update concert set status = #{concertStatusEnum} where concert_id = #{concertId}")
    int updateStatus(String concertId, short concertStatusEnum);

    int update(Concert concert);

    @Update("UPDATE concert SET status = 1 WHERE begin_time <= #{saleTime} AND begin_time > #{now} AND status = 0")
    int task_startConcert(LocalDateTime now,LocalDateTime saleTime);

    @Update("update concert set status = 2 where status in(0,1,3) and begin_time<=#{now}")
    int task_endConcert(LocalDateTime now);
}
