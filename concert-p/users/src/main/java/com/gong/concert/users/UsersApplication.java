package com.gong.concert.users;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

/**
 * @Author ToastFish
 * @Time 2024/10/22
 * 用户模块启动类
 */

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = "com.gong.concert.*.mapper")
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.gong")
public class UsersApplication {
    private static final Logger LOG = LoggerFactory.getLogger(UsersApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UsersApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello",
                env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
    }
}