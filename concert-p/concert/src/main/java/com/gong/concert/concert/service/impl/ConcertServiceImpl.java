package com.gong.concert.concert.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.Method;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gong.concert.common.enums.ConcertStatusEnum;
import com.gong.concert.common.exception.BusinessException;
import com.gong.concert.common.exception.BusinessExceptionEnum;
import com.gong.concert.common.exception.ConcertException;
import com.gong.concert.common.exception.OrderException;
import com.gong.concert.common.resp.PageResult;
import com.gong.concert.common.util.SnowUtil;
import com.gong.concert.concert.dto.QueryConcertByPageDTO;
import com.gong.concert.concert.dto.SaveConcertDTO;
import com.gong.concert.concert.dto.UpdateConcertDTO;
import com.gong.concert.concert.entity.Concert;
import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.entity.Theater;
import com.gong.concert.concert.mapper.ConcertMapper;
import com.gong.concert.concert.mapper.SeatMapper;
import com.gong.concert.concert.mapper.TheaterMapper;
import com.gong.concert.concert.service.ConcertService;
import com.gong.concert.concert.vo.ConcertForUser;
import com.gong.concert.concert.vo.ConcertVO;
import com.gong.concert.feign.clients.OrderClient;
import com.gong.concert.feign.pojo.RejectOrderBatchDTO;
import com.gong.concert.feign.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/11/13
 */
@Service
@Slf4j
public class ConcertServiceImpl implements ConcertService {
    @Autowired
    private TheaterMapper theaterMapper;

    @Autowired
    private ConcertMapper concertMapper;

    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private OrderClient orderClient;

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保事务回滚
    public void saveConcert(SaveConcertDTO dto) {
        log.info("进入新增演唱会Service层");
        // 查询展厅信息
        Theater theater = theaterMapper.getTheaterById(dto.getTheaterId());
        if (theater == null) {
            throw new RuntimeException("展厅不存在");
        }

        // 保存演唱会信息到 concert 表
        Concert concert = new Concert();
        concert.setConcertId(SnowUtil.getSnowflakeNextIdStr());
        concert.setTheaterId(dto.getTheaterId());
        concert.setName(dto.getName());
        concert.setPhoto(dto.getPhoto());
        concert.setLowPrice(dto.getLowPrice());
        concert.setStatus((short) -1);//状态 -1待审核 0 待售 1售卖中 2 停售 3 售罄
        concert.setLocation(theater.getCity());
        concert.setDescribe(dto.getDescribe());
        concert.setNumber(theater.getSeatNum());
        concert.setType(dto.getType());
        concert.setPlayer(dto.getPlayer());
        concert.setCreateTime(LocalDateTime.now());
        concert.setBeginTime(dto.getBeginTime());
//        concert.setCreateUser(LoginBusinessContext.getUsername());
        concert.setCreateUser(dto.getCreateUser());//先写死
        concert.setIsSelected(dto.getIsSelected());

        int i = concertMapper.insertConcert(concert);
        log.info("插入演唱会影响行数:{}",i);
        // 获取演唱会ID
        String concertId = concert.getConcertId();

        // 根据展厅的行数和列数生成座位，并插入到 seats 表
        generateSeats(theater, concertId);
    }

    @Override
    public PageResult pageQuery(QueryConcertByPageDTO dto) {
        log.info("演唱会分页查询进入Service层:{},{},{}",dto.getPage(),dto.getSize(),dto);
        //开始分页查询
        PageHelper.startPage(dto.getPage(), dto.getSize());
        if (dto.getCreateUser().equals("admin")){
            dto.setCreateUser(null); //管理员查询所有
        }
        Page<Concert> page = concertMapper.pageQuery(dto);
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public PageResult pageQueryUser(QueryConcertByPageDTO dto) {
        log.info("用户演唱会分页查询进入Service层:{},{},{}",dto.getPage(),dto.getSize(),dto);
        //开始分页查询
        PageHelper.startPage(dto.getPage(), dto.getSize());
        Page<Concert> page = concertMapper.pageQuery(dto);
        List<Concert> concertList = page.getResult();
        List<ConcertForUser> vo = new ArrayList<>();
        for (Concert concert : concertList) {
            ConcertForUser forUser = new ConcertForUser();
            BeanUtil.copyProperties(concert,forUser);
            int seatNum = seatMapper.selectCount(concert.getConcertId());
            int currentNum = seatMapper.selectCountByStatus(concert.getConcertId(),(short) 0);
            forUser.setSeatNum(seatNum);
            forUser.setCurrentNum(currentNum);
            vo.add(forUser);
        }
        PageResult pageResult = new PageResult(page.getTotal(), vo);
        return pageResult;
    }

    @Override
    public ConcertVO getById(String concertId) {
        log.info("演唱会查单个进入Service层:{}",concertId);
        QueryConcertByPageDTO dto = new QueryConcertByPageDTO();//省力只想写一个sql了
        dto.setConcertId(concertId);
        Page<Concert> concerts = concertMapper.pageQuery(dto);//查出演唱会信息
        Concert concert = concerts.get(0);
        ConcertVO concertVO = new ConcertVO();
        BeanUtil.copyProperties(concert,concertVO);//封装演唱会信息

        List<Seat> seatList = seatMapper.getByConcertId(concertId); //查出演唱会相关座位信息
        concertVO.setSeatList(seatList); //封装座位
        return concertVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保事务回滚
    public int stopSale(String concertId) {
        log.info("演唱会停售进入Service层:{},{}", Method.GET,concertId);
        int flag = 0; //成功标记
        int i = concertMapper.updateStatus(concertId, ConcertStatusEnum.HALT_THE_SALES.getStatus()); //设置停售
        if (i == 0){
            throw new BusinessException(BusinessExceptionEnum.UPDATE_CONCERT_FAIL);
        }
        log.info("开始释放、停售、退款演唱会订单、座位");
        List<Seat> seats = seatMapper.getByConcertId(concertId);
        for (Seat seat : seats) {
            RejectOrderBatchDTO rejectOrderBatchDTO = new RejectOrderBatchDTO();
            rejectOrderBatchDTO.setConcertId(concertId);
            rejectOrderBatchDTO.setRejectionReason("演唱会停售，自动退还订单");
            rejectOrderBatchDTO.setInUrl((short) 1);
            Result result = orderClient.rejectOrderBatch(rejectOrderBatchDTO);
            if(result.getCode()!=1){
                throw new OrderException("订单释放失败"+result.getMsg());
            }
            if (seat.getSeatStatus()==2){ //维修状态
                continue; //跳过这个座位
            }
            seat.setSeatStatus((short) 3); //停售
            int j = seatMapper.updateStatus(seat.getSeatId(),seat.getSeatStatus());
            if (j == 0){
                throw new BusinessException(BusinessExceptionEnum.UPDATE_SEAT_FAIL);
            }
        }
        flag = 1;
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 确保事务回滚
    public int startSale(String concertId) {
        log.info("演唱会启售进入Service层:{},{}", Method.GET,concertId);
        int flag = 0;
        int i = concertMapper.updateStatus(concertId, ConcertStatusEnum.FOR_SALE.getStatus()); //设置停售
        if (i == 0){
            throw new BusinessException(BusinessExceptionEnum.UPDATE_CONCERT_FAIL);
        }
        log.info("启动座位");
        List<Seat> seats = seatMapper.getByConcertId(concertId);
        for (Seat seat : seats) {
            if (seat.getSeatStatus()==2){ //维修状态
                continue; //跳过这个座位
            }
            seat.setSeatStatus((short)0); //启售
            int j = seatMapper.updateStatus(seat.getSeatId(),seat.getSeatStatus());
            if (j == 0){
                throw new BusinessException(BusinessExceptionEnum.UPDATE_SEAT_FAIL);
            }
        }
        flag = 1;
        return flag;
    }

    @Override
    public Concert getByIdFeign(String concertId) {
        log.info("演唱会查单个进入Feign - Service层:{}",concertId);
        QueryConcertByPageDTO dto = new QueryConcertByPageDTO();//省力只想写一个sql了
        dto.setConcertId(concertId);
        Page<Concert> concerts = concertMapper.pageQuery(dto);//查出演唱会信息
        Concert concert = concerts.get(0);
        return concert;
    }

    @Override
    public int updateStatus(String concertId, Short status) {
        return concertMapper.updateStatus(concertId,status);
    }

    @Override
    public void update(UpdateConcertDTO dto) {
        log.info("进入更新演唱会Service层",dto);
        String concertId = dto.getConcertId();
        LocalDateTime beginTime = dto.getBeginTime();
        if (concertId==null || concertId.equals("")){
            throw new ConcertException("更新演唱会-concertId为空");
        }
        if (beginTime.isBefore(LocalDateTime.now())){
            throw new ConcertException("更新演唱会-时间校验失败-"+beginTime);
        }
        Concert concert = new Concert();
        BeanUtil.copyProperties(dto,concert);
        int i = concertMapper.update(concert);
        if (i==0){
            throw new ConcertException("更新失败，更新行数为0");
        }
    }

    private void generateSeats(Theater theater, String concertId) {
        for (int row = 1; row <= theater.getRow(); row++) {
            for (int col = 1; col <= theater.getCol(); col++) {
                Seat seat = new Seat();
                seat.setSeatId(SnowUtil.getSnowflakeNextIdStr());
                seat.setConcertId(concertId);
                seat.setSeatType((short)3);
                seat.setSeatRow(row);
                seat.setSeatCol(col);
                //seat.setSeatNumber("R" + row + "C" + col); // 座位编号
                seat.setSeatStatus((short)0); // 初始状态为可售
                seat.setFee((double)100); // 可以根据需要设置不同的票价
                log.info("本次插入座位信息:{}",seat);
                seatMapper.insertSeat(seat);
            }
        }
    }
}
