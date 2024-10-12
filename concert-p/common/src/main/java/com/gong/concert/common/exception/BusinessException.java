package com.gong.concert.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @Author ToastFish
 * @Time 2024/10/11
 */


@AllArgsConstructor
@Data
public class BusinessException extends RuntimeException{
    private BusinessExceptionEnum e;

    /**
    * 不写入堆栈信息，提高性能
    * */
    @Override
    public Throwable fillInStackTrace(){
        return this;
    }
}
