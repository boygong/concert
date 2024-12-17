package com.gong.concert.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author ToastFish
 * @Time 2024/12/17
 */
@AllArgsConstructor
@Data
public class ConcertException extends RuntimeException{
    private String des;

    /**
     * 不写入堆栈信息，提高性能
     * */
    @Override
    public Throwable fillInStackTrace(){
        return this;
    }
}
