package com.gong.concert.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ToastFish
 * @Time 2024/12/1
 */
@AllArgsConstructor
@Data
public class OrderException extends RuntimeException{
    private String des;

    /**
     * 不写入堆栈信息，提高性能
     * */
    @Override
    public Throwable fillInStackTrace(){
        return this;
    }
}
