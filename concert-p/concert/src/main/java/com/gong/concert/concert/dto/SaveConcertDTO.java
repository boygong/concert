package com.gong.concert.concert.dto;

import com.alibaba.nacos.api.annotation.NacosProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 前端传入保存演唱会实体对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveConcertDTO {
    private String theaterId;
    private String name;
    private Double lowPrice;
    private Short status;
    private String location;
    private String describe;
    private Integer number;
    private Short type;
    private String player;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime beginTime;
    private LocalDateTime createUser;
    private LocalDateTime updateUser;
    private LocalDateTime isSelected;
}
