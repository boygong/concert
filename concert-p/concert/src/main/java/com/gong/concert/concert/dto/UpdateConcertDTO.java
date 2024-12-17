package com.gong.concert.concert.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/12/17
 * @describe 更新演唱会信息入参
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateConcertDTO {
    private String concertId;
    private String name;
    private String photo;
    private Double lowPrice;
    private String describe;
    private Short type;
    private String player;
    private String isSelected;
    private LocalDateTime beginTime;
}
