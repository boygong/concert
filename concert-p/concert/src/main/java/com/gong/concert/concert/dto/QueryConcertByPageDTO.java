package com.gong.concert.concert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 分页查询入参
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryConcertByPageDTO {
    private Integer page;
    private Integer size;

    private String concertId; //演唱会id
    private String theaterId;//关联影厅id
    private String name;//演唱会名称
    private Double highFee;//最高价
    private Double lowFee;//最高价
    private Short status;//状态-1待审核 0 待售 1售卖中 2 停售 3 售罄
    private String location;//冗余字段（所在地）
    private String describe;//演唱会介绍
    private Short type;//演出类型 0演唱会 1英语会 ....
    private String player;//演出人员
    private String createUser; //创建人

    private LocalDateTime beginTime;//演出开始时间
    private LocalDateTime endTime;//结束时间
    private Short isSelected;//是否可以选座，0可选座位 1不可选 2可选等级座位

}
