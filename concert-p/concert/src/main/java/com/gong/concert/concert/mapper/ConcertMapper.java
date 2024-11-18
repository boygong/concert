package com.gong.concert.concert.mapper;

import com.github.pagehelper.Page;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.entity.Concert;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */
public interface ConcertMapper {
    int insertConcert(Concert concert);

    Page<Concert> pageQuery(QueryConcertByPageDTO dto);
}
