package com.gong.concert.order.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.common.exception.OrderException;
import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.util.RedisLockUtil;
import com.gong.concert.common.util.SnowUtil;
import com.gong.concert.feign.clients.BusinessClient;
import com.gong.concert.feign.clients.ConcertClient;
import com.gong.concert.feign.clients.SeatClient;
import com.gong.concert.feign.pojo.Concert2;
import com.gong.concert.feign.pojo.Seat;
import com.gong.concert.order.dto.*;
import com.gong.concert.order.entity.Order;
import com.gong.concert.order.entity.OrderDetail;
import com.gong.concert.order.mapper.OrderDetailMapper;
import com.gong.concert.order.mapper.OrderMapper;
import com.gong.concert.order.service.OrderService;
import com.gong.concert.order.vo.CreateOrderVO;
import com.gong.concert.order.vo.OrderDetailVO;
import com.gong.concert.order.vo.PageQueryVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConcertClient concertClient;
    @Autowired
    private BusinessClient businessClient;


    @Override
    @GlobalTransactional
    public CreateOrderVO createOrder(CreateOrderDTO dto) {
        log.info("进入创建订单createConfirm的Service层:{}",dto);
        String userId = dto.getUserId();
        String concertId = dto.getConcertId();
        Short isSelected = dto.getIsSelected();
        List<String> seatIdList = dto.getSeatIdList();
        Integer seatNum = dto.getSeatNum();
        double allAmount = 0;//支付总金额

        if (userId ==null || userId.equals("")){
            throw new BusinessException(BusinessExceptionEnum.USERID_IS_NULL);
        }
        if (concertId==null || concertId.equals("")){
            throw new BusinessException(BusinessExceptionEnum.CONCERTID_IS_NULL);
        }
        log.info("OpenFeign获取演唱会信息:{}",concertClient.getByIdFeign(concertId));
        Concert2 concert = concertClient.getByIdFeign(concertId);
        log.info("调用feign获取到的演唱会信息:{}",concert);
        if (concert.getStatus()!=(short)1){
            throw new OrderException("演唱会状态异常无法预约");
        }
        List<Seat> seatList = new ArrayList<>();

        if (isSelected == 0){  //可选座位
            if (seatIdList.isEmpty() || seatNum == 0){ //座位数为空
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
            }
        }
        else  if (isSelected == 1){ //不可选座位
            seatList = seatClient.getByNum(concertId,seatNum);

        }
        if (seatList.size() != seatNum ||seatList.size()==0){
            //更新演唱会状态为售罄
            concertClient.updateStatus(concertId,(short) 3);
            throw new OrderException("座位余票不足，座位只剩"+seatList.size()+",实际购买"+seatNum);
        }
        /**更新座位信息*/
        for (Seat seat : seatList) {
            // 加锁操作：每次获取座位锁
            boolean lockAcquired = redisLockUtil.lock("seat_lock_" + seat.getSeatId(), 30, 10, 2); // 锁超时时间30秒，最大等待10秒，重试间隔2秒
            log.info("执行Redis加锁操作");
            if (!lockAcquired) {
                throw new OrderException("座位 " + seat.getSeatId() + " 正在处理中，请稍后再试");
            }
            Short seatStatus = seatClient.getById(seat.getSeatId()).getSeatStatus();
            if (isSelected==0 && seatStatus != (short) 0){ //加锁后再次查询并判断座位状态，方式上一个请求购完票之后，新线程抢到锁导致超卖
                throw new OrderException("创建订单失败-你所选座位已售出");
            }
            boolean flag = seatClient.updateStatus(seat,(short)5); //设置座位状态为待支付
            if (flag==false){
                throw new OrderException("更新座位信息失败");
            }
           // int x = 1/0; //测试分布式事务回滚
            allAmount += seat.getFee();
        }
        /**新增订单信息**/
        Order orderDb = new Order();
        /*封装订单信息start*/
        orderDb.setOrderId(SnowUtil.getSnowflakeNextIdStr());
        orderDb.setUserId(String.valueOf(dto.getUserId())); //先写死
        orderDb.setConcertId(concertId);
        orderDb.setAddressBookId(null);
        orderDb.setPayStatus((short)0);
        orderDb.setAmount(allAmount);
        orderDb.setRemark(dto.getRemark());////需要查演唱会数据
        orderDb.setPhone("15342907459"); ////需要登录之后在线程中获取，先写死
        orderDb.setAddress(null); ////需要新增地址簿各功能
        orderDb.setUserName("头号玩家"); //需要登录之后在线程中获取，先写死
        orderDb.setConsignee(null); //需要新增地址簿各功能
        orderDb.setCreateTime(LocalDateTime.now());
        orderDb.setOrderStatus((short) 0); //设置待确认状态
        orderDb.setBeginTime(concert.getBeginTime()); //需要新增模块之间查询演唱会功能
        /*封装订单信息end*/
        int i = orderMapper.insert(orderDb);
        if (i==0){
            throw new OrderException("创建订单异常");
        }
        /**新增订单明细信息**/
        List<OrderDetail> detailList = new ArrayList<>();
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
            detailList.add(detail);
            int j = orderDetailMapper.insert(detail);
            if (j == 0){
                throw new OrderException("新增订单明细失败");
            }
        }
        /**封装返回订单结果**/
        CreateOrderVO vo = new CreateOrderVO();
        vo.setOrderId(orderDb.getOrderId());
        vo.setUserName("头号玩家");
        vo.setConcertName(concert.getName());
        vo.setAmount(allAmount);
        vo.setBeginTime(concert.getBeginTime());
        vo.setOrderDetails(detailList);
        vo.setCreateTime(LocalDateTime.now());
        vo.setImage(concert.getPhoto());
        return vo;
    }

    @Override
    public String confirm(ConfirmOrderDTO dto) {
        log.info("确认订单进入Service层:{}",dto);
        String orderId = dto.getOrderId();
        Short payMethod = dto.getPayMethod();
        Double amount = dto.getAmount();
        if (orderId==null ||orderId==""){
            throw new OrderException("确认订单orderId为空");
        }
        if (payMethod==null){
            throw new OrderException("支付方式为空");
        }
        if (payMethod<(short) 0 ){
            throw new OrderException("支付方式不合法");
        }
        if (amount<0){
            throw new OrderException("支付金额非法");
        }
        //查询出订单信息
        Order order = orderMapper.selectById(orderId);
        if (order==null){
            throw new OrderException("未检索到订单信息");
        }
        if (Math.abs(order.getAmount()-amount)>0.000001){
            throw new OrderException("支付金额验证不通过");
        }
        if (order.getOrderStatus()==(short) 1){
            throw new OrderException("该订单已确认");
        }
        /**对订单信息进行修改*/
        Order orderDb = new Order();
        orderDb.setOrderId(orderId);
        orderDb.setOrderStatus((short) 1);//已确认
        orderDb.setPayMethod(payMethod); //设置支付方式
        orderDb.setPayStatus((short)1); //直接设置为已支付状态
        int i = orderMapper.update(orderDb);
        if (i != 1){
            throw new OrderException("确认订单失败");
        }
        return orderId;
    }

    @Override
    public PageResult pageQuery(OrderPageQueryDTO dto) {
        log.info("演唱会分页查询进入Service层:{},{},{}",dto.getPage(),dto.getSize(),dto);
        //开始分页查询
        PageHelper.startPage(dto.getPage(), dto.getSize());
//        String businessName = dto.getCreateUser();  //商家用户名
//        Result businessRsp = businessClient.getOne(businessName);
//        BusinessVO data = (BusinessVO) businessRsp.getData();
//        Short identity = data.getIdentity();
//        if (identity == (short) 0){ //为管理员
//            dto.setCreateUser(null);
//        }
        Page<Order> page = orderMapper.select(dto);
        List<Order> orders = page.getResult();
        List<PageQueryVO> list = new ArrayList<>();
        for (Order order : orders) {
            int num = orderDetailMapper.selectCountByOrderId(order.getOrderId());
            PageQueryVO pageQueryVO = new PageQueryVO();
            BeanUtil.copyProperties(order,pageQueryVO);
            pageQueryVO.setDetailNum(num);
            list.add(pageQueryVO);
        }
        PageResult pageResult = new PageResult(page.getTotal(),list);
        return pageResult;
    }

    @Override
    @GlobalTransactional
    public void cancelOrder(CancelOrderDTO dto) {
        if (dto.getOrderId() ==null || dto.getOrderId().isEmpty()){
            throw new OrderException("传入的订单号为空");
        }
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order==null){
            throw new OrderException("未查询到订单信息"+dto.getOrderId());
        }
        /**更新订单*/
        Order orderDb = new Order();
        orderDb.setOrderId(dto.getOrderId());
        orderDb.setCancelReason(dto.getCancelReason());
        orderDb.setOrderStatus((short) 4);//已取消
        orderDb.setCancelTime(LocalDateTime.now());
        orderDb.setUpdateTime(LocalDateTime.now());
        if (order.getPayStatus()==(short)1){
            orderDb.setPayStatus((short) 2); //退款
        }
        int update = orderMapper.update(orderDb);
        if (update!=1){
            throw new OrderException("更新订单信息失败");
        }
        /**更新座位状态*/
        List<String> seatIds =  orderDetailMapper.selectSeatIdByOrderId(order.getOrderId());
        for (String seatId : seatIds) {
            int i = seatClient.updateStatusBySeatId(seatId,(short) 0);
            if (i!=1){
                throw new OrderException("更新座位"+seatId+"信息失败");
            }
        }
    }

    @Override
    public void rejectOrder(RejectOrderDTO dto) {
        if (dto.getOrderId() ==null ||dto.getOrderId().equals("")){
            throw new OrderException("传入的订单号为空");
        }
        Order order = orderMapper.selectById(dto.getOrderId());
        if (order==null){
            throw new OrderException("未查询到订单信息"+dto.getOrderId());
        }
        /**更新订单*/
        Order orderDb = new Order();
        orderDb.setOrderId(dto.getOrderId());
        orderDb.setCancelReason(dto.getRejectionReason());
        orderDb.setOrderStatus((short) 5);//已拒绝
        orderDb.setCancelTime(LocalDateTime.now());
        orderDb.setUpdateTime(LocalDateTime.now());
        if (order.getPayStatus()==(short)1){
            orderDb.setPayStatus((short) 2); //退款
        }
        int update = orderMapper.update(orderDb);
        if (update!=1){
            throw new OrderException("更新订单信息失败");
        }
        /**更新座位状态*/
        List<String> seatIds =  orderDetailMapper.selectSeatIdByOrderId(order.getOrderId());
        for (String seatId : seatIds) {
            int i = seatClient.updateStatusBySeatId(seatId,(short) 0);
            if (i!=1){
                throw new OrderException("更新座位"+seatId+"信息失败");
            }
        }
    }

    @Override
    public OrderDetailVO detail(String orderId) {
        if (orderId==null ||orderId.equals("")){
            throw new OrderException("查询订单明细传入的订单id为空");
        }
        Order order = orderMapper.selectById(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(order,orderDetailVO);
        orderDetailVO.setDetailNum(orderDetailMapper.selectCountByOrderId(orderId));
        orderDetailVO.setOrderDetails(orderDetails);
        return orderDetailVO;
    }

    @Override
    public void rejectOrderBatch(RejectOrderBatchDTO dto) {
        String rejectionReason = dto.getRejectionReason();
        String concertId = dto.getConcertId();
        List<String> orderIds = dto.getOrderId();
        List<String> updateSeatId = new ArrayList<>();
        if (orderIds==null || orderIds.size() == 0){
            Order orderDb = new Order();
            orderDb.setRejectionReason(rejectionReason);
            orderDb.setCancelTime(LocalDateTime.now());
            orderDb.setConcertId(concertId);
            orderDb.setOrderStatus((short) 5); //退订
            int i = orderMapper.update(orderDb);
            List<String> orderIdDbs = orderMapper.selectOrderIdByConcertId(concertId);
            checkNum(updateSeatId, i, orderIdDbs);
        }else {
            for (String orderId : orderIds) {
                Order orderDb = new Order();
                orderDb.setOrderId(orderId);
                orderDb.setOrderStatus((short) 5); //退订
                orderDb.setRejectionReason(rejectionReason);
                orderDb.setCancelTime(LocalDateTime.now());
                int i = orderMapper.update(orderDb);
                List<String> orderIdDbs = orderDetailMapper.selectSeatIdByOrderId(orderId);
                checkNum(updateSeatId, i, orderIdDbs);
            }
        }

        if(dto.getInUrl()==null){ //不是feign调入，需处理座位释放
            for (String s : updateSeatId) {
                seatClient.updateStatusBySeatId(s,(short) 0);//启用
            }
        }
    }

    //检查订单明细
    private void checkNum(List<String> updateSeatId, int i, List<String> orderIdDbs) {
        for (String orderIdDb : orderIdDbs) {
            List<String> seatIdByOrderId = orderDetailMapper.selectSeatIdByOrderId(orderIdDb);
            for (String s : seatIdByOrderId) {
                updateSeatId.add(s);
            }
        }
        log.info("更新订单影响行数:{}", i);
        if(i != updateSeatId.size()){
            throw new OrderException("订单明细数与需更新的座位数不一致");
        }
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
