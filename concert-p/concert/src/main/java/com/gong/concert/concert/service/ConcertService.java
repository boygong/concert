package com.gong.concert.concert.service;

import com.gong.concert.common.resp.PageResult;
import com.gong.concert.concert.dto.AuditConcertDTO;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.dto.SaveConcertDTO;
import com.gong.concert.concert.dto.UpdateConcertDTO;
import com.gong.concert.concert.entity.Concert2;
import com.gong.concert.concert.vo.ConcertVO;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */

public interface ConcertService {
    void saveConcert(SaveConcertDTO dto);

    PageResult pageQuery(QueryConcertByPageDTO dto);
    PageResult pageQueryUser(QueryConcertByPageDTO dto);

    ConcertVO getById(String concertId);

    int stopSale(String concertId);

    int startSale(String concertId);

    Concert2 getByIdFeign(String concertId);

    int updateStatus(String concertId, Short status);


    void update(UpdateConcertDTO dto);

    void audit(AuditConcertDTO dto);
}
