package com.me.houses.config;

import com.me.houses.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/22.
 */
//使用@Configuration表示这是一个JavaConfig配置类
//其中标注的@Bean的方法，其返回值将作为一个bean定义注册到Spring的IoC容器
@Configuration
public class FilterBeanConfig {

    /**
     * 构造filter
     * 配置拦截url pattern
     */
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LogFilter());
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("*");
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }

}
