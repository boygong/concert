package com.gong.concert.common.exception;

import com.gong.concert.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author ToastFish
 * @Time 2024/10/11
 * @Descripe 统一异常出出力、数据预处理等
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 所有异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) throws Exception {
        // LOG.info("seata全局事务ID: {}", RootContext.getXID());
        // // 如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
        // if (StrUtil.isNotBlank(RootContext.getXID())) {
        //     throw e;
        // }
        Result result = new Result();
        log.error("系统异常：", e);
        result.setCode(999);
        result.setMsg("系统出现异常，请联系管理员");
        return result;
    }

    /**
     * 业务异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result exceptionHandler(BusinessException e) {
        Result result = new Result();
        log.error("业务异常:", e.getE().getDesc());
        result.setCode(999);
        result.setMsg(e.getE().getDesc());
        return result;
    }
    /**
     * 校验异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result exceptionHandler(BindException e) {
        Result result = new Result();
        log.error("校验异常:", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        result.setCode(0);
        result.setMsg(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return result;
    }

    @ExceptionHandler(value = OrderException.class)
    @ResponseBody
    public Result exceptionHandler(OrderException e){
        // LOG.info("seata全局事务ID: {}", RootContext.getXID());
        // // 如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
        // if (StrUtil.isNotBlank(RootContext.getXID())) {
        //     throw e;
        // }
        Result result = new Result();
        log.error("订单业务异常:", e.getDes());
        result.setCode(555);
        result.setMsg("订单业务异常:"+e.getDes());
        return result;
    }

    @ExceptionHandler(value = ConcertException.class)
    @ResponseBody
    public Result exceptionHandler(ConcertException e){
        // LOG.info("seata全局事务ID: {}", RootContext.getXID());
        // // 如果是在一次全局事务里出异常了，就不要包装返回值，将异常抛给调用方，让调用方回滚事务
        // if (StrUtil.isNotBlank(RootContext.getXID())) {
        //     throw e;
        // }
        Result result = new Result();
        log.error("演唱会业务异常:", e.getDes());
        result.setCode(555);
        result.setMsg("演唱会业务异常:"+e.getDes());
        return result;
    }

//    @ExceptionHandler(value = SQLException.class)
//    @ResponseBody
//    public Result exceptionHandler(SQLException e) {
//        Result result = new Result();
//        log.error("数据库异常：{}", e.fillInStackTrace());
//        result.setCode(999);
//        result.setMsg("数据库异常，请联系管理员");
//        return result;
//    }
}
