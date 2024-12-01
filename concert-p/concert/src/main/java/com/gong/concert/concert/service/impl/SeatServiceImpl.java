package com.gong.concert.concert.service.impl;

import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.mapper.SeatMapper;
import com.gong.concert.concert.service.SeatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Seat> getByNum(String concertId, Integer num) {
        return seatMapper.getByNum(concertId, num);
    }

    @Override
    public boolean updateStatus(Seat seat, Short status) {
        if (!seat.getSeatId().equals("") && seat.getSeatId() != null) {
            int i = seatMapper.updateStatus(seat.getConcertId(), seat.getSeatId(), status);
            if (i == 1) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
