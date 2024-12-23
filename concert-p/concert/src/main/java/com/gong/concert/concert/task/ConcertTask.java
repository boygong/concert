package com.gong.concert.concert.task;


import cn.hutool.core.date.DateUtil;
import com.gong.concert.concert.mapper.ConcertMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ConcertTask {
    @Autowired
    private ConcertMapper concertMapper;
    //@Scheduled(cron = "0/5 * * * * ?")
    public void testTask(){
      log.info("ConcertTask Test");
    }

    /**
    * @description: 调度任务:每5分钟开启待售状态下的演唱会
    * */
    @Scheduled(cron = "0 */1 * * * *")
    public void startConcertTask(){
        log.info("------调度任务:开启待售状态下的演唱会 开始执行------");
        long current = DateUtil.current();
        log.info("更新演唱会状态");
        int i = concertMapper.task_startConcert(LocalDateTime.now(),LocalDateTime.now().plusDays(3));
        log.info("更新行数:{}",i);
        log.info("------调度任务:开启待售状态下的演唱会 结束执行，耗时:{}",(DateUtil.current()-current)+"秒------");
    }

    /**
     * @description: 调度任务:每5分钟停止在售、售罄状态下的演唱会
     * */
    @Scheduled(cron = "0 */1 * * * *")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void endConcertTask(){
        log.info("------调度任务:停止在售、售罄状态下的演唱会 开始执行------");
        long current = DateUtil.current();
        log.info("更新演唱会状态");
        int i = concertMapper.task_endConcert(LocalDateTime.now());
        log.info("------调度任务:停止在售、售罄状态下的演唱会 结束执行，耗时:{}",(DateUtil.current()-current)+"秒------");

    }
}
