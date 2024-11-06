package com.gong.concert.concert;

import com.gong.concert.feign.config.DefaultFeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

/**
 * @Author ToastFish
 * @Time 2024/11/6
 * 演唱会门票启动类
 */

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = "com.gong.concert.*.mapper")
@EnableFeignClients(basePackages = "com.gong.concert.feign.clients",defaultConfiguration = DefaultFeignConfiguration.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.gong")
public class ConcertApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ConcertApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ConcertApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello",
                env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
    }
}