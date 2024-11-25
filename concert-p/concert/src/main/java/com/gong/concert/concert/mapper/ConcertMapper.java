package com.gong.concert.concert.mapper;

import com.github.pagehelper.Page;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.entity.Concert;
import org.apache.ibatis.annotations.Update;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */
public interface ConcertMapper {
    int insertConcert(Concert concert);

    Page<Concert> pageQuery(QueryConcertByPageDTO dto);

    @Update("update concert set status = #{concertStatusEnum} where concert_id = #{concertId}")
    int updateStatus(String concertId, short concertStatusEnum);
}
