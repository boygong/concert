package com.gong.concert.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author ToastFish
 * @Time 2024/12/2
 */
@Configuration
@Slf4j
public class RedisConfig {

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        // 手动配置 Redis 连接工厂
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName("127.0.0.1");
//        configuration.setPort(15112);
//        configuration.setPassword(RedisPassword.of("123456"));
//        configuration.setDatabase(0);
//        return new LettuceConnectionFactory(configuration);
//    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("开始创建Redis模板对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置Redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用 StringRedisSerializer 来序列化 key
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 使用 Jackson2JsonRedisSerializer 来序列化 value
        Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(jsonSerializer);

        // 设置默认的序列化方式
        redisTemplate.setDefaultSerializer(jsonSerializer);
        return redisTemplate;
    }
}