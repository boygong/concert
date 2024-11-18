package com.gong.concert.concert.mapper;

import com.github.pagehelper.Page;
import com.gong.concert.concert.dto.QueryConcertByPage;
import com.gong.concert.concert.entity.Concert;
import com.gong.concert.concert.service.ConcertService;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */
public interface ConcertMapper {
    int insertConcert(Concert concert);

    Page<Concert> pageQuery(QueryConcertByPage dto);
}
