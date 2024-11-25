package com.gong.concert.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ToastFish
 * @Time 2024/11/25
 */
@AllArgsConstructor
@NoArgsConstructor

public enum ConcertStatusEnum {
    AUDIT_FAILED((short) -2), //审核不通过
    PENDING_REVIEW((short) -1),//待审核
    FOR_SALE((short) 0),//待售
    ON_SALE((short) 1),//售卖中
    HALT_THE_SALES((short) 2), //停售
    SELL_OUT((short) 3); //售罄

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    private Short status;
}
