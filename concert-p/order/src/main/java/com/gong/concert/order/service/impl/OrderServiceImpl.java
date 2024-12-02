package com.gong.concert.order.service.impl;


import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.common.exception.OrderException;
import com.gong.concert.common.util.RedisLockUtil;
import com.gong.concert.common.util.SnowUtil;
import com.gong.concert.feign.clients.SeatClient;
import com.gong.concert.feign.pojo.Seat;
import com.gong.concert.order.dto.CreateOrderDTO;

import com.gong.concert.order.entity.Order;
import com.gong.concert.order.entity.OrderDetail;
import com.gong.concert.order.mapper.OrderDetailMapper;
import com.gong.concert.order.mapper.OrderMapper;
import com.gong.concert.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/29
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeatClient seatClient;
    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private RedisLockUtil redisLockUtil;


    @Override
    @GlobalTransactional
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
        List<Seat> seatList = new ArrayList<>();
        if (isSelected == 0){  //可选座位
            if (seatIdList.size()==0||seatIdList==null||seatNum==0){ //座位数为空
                throw new BusinessException(BusinessExceptionEnum.SEATNUM_IS_NULL);
            }
            if (seatIdList.size()!=seatNum){  //座位列表与座位数不一致
                throw new BusinessException(BusinessExceptionEnum.SEATNUM_IS_ERROR);
            }
            //遍历座位id列表查询座位信息
            for (String seatId : seatIdList) {
                Seat seat = seatClient.getById(seatId);
                log.info("查询出的座位信息,{}",seat);
                Short seatStatus = seat.getSeatStatus();
                Integer row = seat.getSeatRow();
                Integer col = seat.getSeatCol();
                checkSeatStatus(seatStatus, row, col);
                seatList.add(seat);
                String SeatLockStr = seat.getSeatId(); //创建座位锁字符串(暂时用座位id)
                //TODO 向Redis中存入锁字符串
                boolean flag = true; /**存入Redis是否成功，目前默认成功*/
                if(!flag){
                    return false;
                }
            }
        }
        else  if (isSelected == 1){ //不可选座位
            seatList = seatClient.getByNum(concertId,seatNum);

        }
        if (seatList.size() != seatNum ||seatList.size()==0){
            throw new OrderException("座位余票不足，座位只剩"+seatList.size()+",实际购买"+seatNum);
        }
        double allAmount = 0;//支付总金额
        /**更新座位信息*/
        for (Seat seat : seatList) {
            // 加锁操作：每次获取座位锁
            boolean lockAcquired = redisLockUtil.lock("seat_lock_" + seat.getSeatId(), 30, 10, 2); // 锁超时时间30秒，最大等待10秒，重试间隔2秒
            log.info("执行Redis加锁操作");
            if (!lockAcquired) {
                throw new OrderException("座位 " + seat.getSeatId() + " 正在处理中，请稍后再试");
            }
            boolean flag = seatClient.updateStatus(seat,(short)5); //设置座位状态为待支付
            if (flag==false){
                throw new OrderException("更新座位信息失败");
            }
           // int x = 1/0; 测试分布式事务回滚
            allAmount += seat.getFee();
        }
        /**新增订单信息**/
        Order orderDb = new Order();
        /*封装订单信息start*/
        orderDb.setOrderId(SnowUtil.getSnowflakeNextIdStr());
        orderDb.setUserId(String.valueOf((short)1)); //先写死
        orderDb.setAddressBookId(orderDb.getAddressBookId());
        orderDb.setPayStatus((short)0);
        orderDb.setAmount(allAmount);
        orderDb.setRemark(dto.getRemark());////需要查演唱会数据
        orderDb.setPhone(null); ////需要查用户信息
        orderDb.setAddress(null); ////需要新增地址簿各功能
        orderDb.setUserName("头号玩家"); //先写死
        orderDb.setConsignee(null); //需要新增地址簿各功能
        orderDb.setCreateTime(LocalDateTime.now());
        orderDb.setOrderStatus((short) 0); //设置待确认状态
        orderDb.setBeginTime(null); //需要新增模块之间查询演唱会功能
        /*封装订单信息end*/
        int i = orderMapper.insert(orderDb);
        if (i==0){
            throw new OrderException("创建订单异常");
        }
        /**新增订单明细信息**/
        for (Seat seat : seatList) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderDetailId(SnowUtil.getSnowflakeNextIdStr());
            detail.setName("等待成功");
            detail.setCol(seat.getSeatCol());
            detail.setRow(seat.getSeatRow());
            detail.setAmount(seat.getFee());
            detail.setOrderId(orderDb.getOrderId());
            detail.setSeatId(seat.getSeatId());
            log.info("订单详细信息：{}",detail);
            int j = orderDetailMapper.insert(detail);
            if (j == 0){
                throw new OrderException("新增订单明细失败");
            }
        }
        /**封装返回订单结果**/
        return false;
    }

    private static void checkSeatStatus(Short seatStatus, Integer row, Integer col) {
        if (seatStatus ==1){
            throw new OrderException( row + "行"+ col +"列" +"的座位已被停用");
        }else if (seatStatus ==2){
            throw new OrderException( row + "行"+ col +"列" + "座位正在被维修");
        }else if (seatStatus ==3){
            throw new OrderException( row + "行"+ col +"列" + "座位已经停用");
        }else if (seatStatus ==6 || seatStatus ==5){
            throw new OrderException( row + "行"+ col +"列" + "座位已经被售出");
        }
    }
}
