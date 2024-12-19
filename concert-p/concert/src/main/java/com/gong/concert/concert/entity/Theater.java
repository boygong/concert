package com.gong.concert.concert.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 剧场实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Theater implements Serializable {
    private String theaterId; //影厅id
    private String name;//影厅名称
    private Integer seatNum;//影厅座位数
    private String latitude;//维度
    private String longitude;//经度
    private String city;//所在城市
    private Integer row;//宽度有多少个座位
    private Integer col;//长度有多少个座位
}
