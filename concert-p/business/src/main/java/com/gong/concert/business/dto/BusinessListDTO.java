package com.gong.concert.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListDTO {
    private String type; //获取类型0为只获取商家，1为只获取管理员，2为全部获取
}
