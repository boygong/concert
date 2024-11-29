package com.gong.concert.concert.service.impl;

import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.mapper.SeatMapper;
import com.gong.concert.concert.service.SeatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@Service
@Slf4j
public class SeatServiceImpl implements SeatService {
    @Resource
    private SeatMapper seatMapper;
    @Override
    public Seat getById(String seatId) {
        return seatMapper.getBySeatId(seatId);
    }
}
