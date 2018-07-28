package com.me.house.biz.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//使用@Configuration表示这是一个JavaConfig配置类
//其中标注的@Bean的方法，其返回值将作为一个bean定义注册到Spring的IoC容器
@Configuration
public class DruidConfig {

    //自动将配置文件中spring.druid为前缀的属性注入到DruidDataSource的对应属性中
    @ConfigurationProperties(prefix = "spring.druid")
    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return dataSource;
    }

    @Bean
    public Filter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(5000); // exe time exceeds 5s is regarded as a slow sql
        statFilter.setLogSlowSql(true); //logging for the slow sql execution
        statFilter.setMergeSql(true);
        return statFilter;
    }

    /* Web UI监控数据库的各种状态 */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

}
