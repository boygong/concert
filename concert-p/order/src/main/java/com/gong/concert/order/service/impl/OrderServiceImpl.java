package com.gong.concert.order.service.impl;

import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.feign.clients.SeatClient;
import com.gong.concert.feign.pojo.Seat;
import com.gong.concert.order.dto.CreateOrderDTO;

import com.gong.concert.order.mapper.OrderMapper;
import com.gong.concert.order.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeatClient seatClient;
    @Override
    public boolean createOrder(CreateOrderDTO dto) {
        log.info("进入创建订单createConfirm的Service层:{}",dto);
        String userId = dto.getUserId();
        String concertId = dto.getConcertId();
        Short isSelected = dto.getIsSelected();
        List<String> seatIdList = dto.getSeatIdList();
        Integer seatNum = dto.getSeatNum();
        String addressBookId = dto.getAddressBookId();
        String remark = dto.getRemark();

        if (userId ==null || userId.equals("")){
            throw new BusinessException(BusinessExceptionEnum.USERID_IS_NULL);
        }
        if (concertId==null || concertId==""){
            throw new BusinessException(BusinessExceptionEnum.CONCERTID_IS_NULL);
        }
        if (isSelected == 0){  //可选座位
            if (seatIdList.size()==0||seatIdList==null||seatNum==0){ //座位数为空
                throw new BusinessException(BusinessExceptionEnum.SEATNUM_IS_NULL);
            }
            if (seatIdList.size()!=seatNum){  //座位列表与座位数不一致
                throw new BusinessException(BusinessExceptionEnum.SEATNUM_IS_ERROR);
            }
            List<Seat> seatList = new ArrayList<>();
            //遍历座位id列表查询座位信息
            for (String seatId : seatIdList) {
                Seat seat = seatClient.getById(seatId);
                log.info("查询出的座位信息,{}",seat);
                //Todo 调用openFeign查询座位信息
            }
        }
        else  if (isSelected == 1){ //不可选座位

        }
        return false;
    }
}
