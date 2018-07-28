package com.me.houses.autoconfig;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*由于该类位于@SpringBootApplication(HousesApplication)为root的目录下
* 因此不必把这个AutoConfiguration注册到spring.factories文件中
* springboot也会扫描到@Configuration注解，然后进行自动配置
*/
@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class) //自动注入这个HttpClientProperties bean
public class HttpClientAutoConfiguration {
    private final HttpClientProperties properties;

    public HttpClientAutoConfiguration(HttpClientProperties properties){
        this.properties = properties;
    }

    /*
     * HttpClient bean的定义与创建
     */
    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(properties.getConnectTimeout())
                .setSocketTimeout(properties.getSocketTimeout())
                .build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setUserAgent(properties.getAgent())
                .setMaxConnPerRoute(properties.getMaxConnPerRoute())
                .setMaxConnTotal(properties.getMaxConnTotal())
                .setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                .build();
        return client;
    }
}
