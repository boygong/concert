package com.gong.concert.common.interceptor;

import com.gong.concert.common.json.JacksonObjectMapper;
import io.seata.integration.http.WebMvcConfigurerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    //扩展Spring MVC框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器..时间格式管理YYYY-MM-DD HH-MM-SS");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //需要为消息装换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自己的消息转换器加入到容器中，0代表优先使用
        converters.add(0,converter);
    }
}
