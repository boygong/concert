package com.gong.concert.concert.mapper;

import com.gong.concert.concert.entity.Theater;
import org.apache.ibatis.annotations.Select;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 * 展厅映射层
 */
public interface TheaterMapper {
    /*
     * @description:更据id查询展厅信息
     * @author: gongyuankang
     * @date: 2024/11/13 14:57
     * @return: com.gong.concert.concert.entity.Theater
    */
    @Select("select * from theater where theater_id = #{theaterId}")
    Theater getTheaterById(String theaterId);
}
