package com.gong.concert.concert.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.dto.SaveConcertDTO;
import com.gong.concert.concert.vo.ConcertVO;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */

public interface ConcertService {
    void saveConcert(SaveConcertDTO dto);

    PageResult pageQuery(QueryConcertByPageDTO dto);

    ConcertVO getById(String concertId);
}
