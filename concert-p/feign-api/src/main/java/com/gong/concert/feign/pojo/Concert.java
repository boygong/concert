package com.gong.concert.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 演唱会实体类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Concert {
    private String concertId; //演唱会id
    private String theaterId;//关联影厅id
    private String name;//演唱会名称
    private String photo; //演唱会图片
    private Double lowPrice;//演唱会最低价格
    private Short status;//状态 0 待售 1售卖中 2 停售 3 售罄
    private String location;//冗余字段（所在地）
    private String describe;//演唱会介绍
    private Integer number;//演唱会座位数
    private Short type;//演出类型 0演唱会 1英语会 ....
    private String player;//演出人员
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
    private LocalDateTime beginTime;//演出开始时间
    private String createUser;//创建人
    private String updateUser;//更新人
    private Short isSelected;//是否可以选座，0可选座位 1不可选 2可选等级座位
}
