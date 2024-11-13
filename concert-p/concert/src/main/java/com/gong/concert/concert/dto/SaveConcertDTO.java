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
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private LocalDateTime beginTime;//演出开始时间
    private String createUser;//创建人
    private String updateUser;//更新人
    private Short isSelected;//是否可以选座，0可选座位 1不可选 2可选等级座位
}
