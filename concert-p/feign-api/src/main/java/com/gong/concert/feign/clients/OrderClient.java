package com.gong.concert.feign.clients;

import com.gong.concert.feign.pojo.RejectOrderBatchDTO;
import com.gong.concert.feign.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author ToastFish
 * @Time 2024/12/6
 */
@FeignClient(name = "order",url = "${open-feign.order.api-url}/order/order")
public interface OrderClient {
    @PostMapping("/rejectOrderBatch")
    public Result rejectOrderBatch(@RequestBody RejectOrderBatchDTO dto);
}
