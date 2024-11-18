package com.gong.concert.concert.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.concert.dto.QueryConcertByPage;
import com.gong.concert.concert.dto.SaveConcertDTO;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */

public interface ConcertService {
    void saveConcert(SaveConcertDTO dto);

    PageResult pageQuery(QueryConcertByPage dto);
}
