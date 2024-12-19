package com.gong.concert.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 缓存管理
     *
     * @return 返回缓存管理信息
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                // 默认没有特殊指定的缓存，设置失效时间为1天
                .entryTtl(Duration.ofDays(1))
                // 在缓存名称前加上前缀
                .computePrefixWith(cacheName -> "default:" + cacheName);
        log.info("设置redis缓存的默认失效时间，失效时间默认为：{}天", defaultCacheConfig.getTtl().toDays());
        // 针对不同cacheName，设置不同的失效时间，map的key是缓存名称（注解设定的value/cacheNames），value是缓存的失效配置
        Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<String, RedisCacheConfiguration>(8);
        // 设定失效时间为1小时
        initialCacheConfiguration.put("userCache", getDefaultSimpleConfiguration().entryTtl(Duration.ofHours(1)));
        // 设定失效时间为10分钟
        initialCacheConfiguration.put("userCache1", getDefaultSimpleConfiguration().entryTtl(Duration.ofMinutes(10)));
        // 设定失效时间为12小时
        initialCacheConfiguration.put("userCache2", getDefaultSimpleConfiguration().entryTtl(Duration.ofHours(12)));
        // ...如果有其他的不同cacheName需要控制失效时间，以此类推即可进行添加
        return RedisCacheManager.builder(redisConnectionFactory)
                // 设置缓存默认失效时间配置，也就是动态或者未指定的缓存将会使用当前配置
                .cacheDefaults(defaultCacheConfig)
                // 不同不同cacheName的个性化配置
                .withInitialCacheConfigurations(initialCacheConfiguration).build();
    }
    /**
     * 覆盖默认的构造key[默认拼接的时候是两个冒号（::）]，否则会多出一个冒号
     *
     * @return 返回缓存配置信息
     */
    private RedisCacheConfiguration getDefaultSimpleConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().computePrefixWith(cacheName -> cacheName + ":");
    }
}