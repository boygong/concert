package com.gong.concert.concert.service;

import com.gong.concert.concert.entity.Seat;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
public interface SeatService {
    Seat getById(String seatId);

    List<Seat> getByNum(String concertId,Integer num);

    boolean updateStatus(Seat seat, Short status);
}
