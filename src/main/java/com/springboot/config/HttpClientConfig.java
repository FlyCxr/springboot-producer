package com.springboot.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(value= "classpath:httpClient.properties")
public class HttpClientConfig {

    @Bean(name="httpClientConnectionManager")
    public HttpClientConnectionManager getHttpClientConnectionManager(@Value("${http.pool.maxTotal}")String maxTotal,
                                                                      @Value("${http.pool.defaultMaxPerRoute}")String defaultMaxPerRoute){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(Integer.valueOf(maxTotal));
        manager.setDefaultMaxPerRoute(Integer.valueOf(defaultMaxPerRoute));
        return manager;
    }

    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager") HttpClientConnectionManager httpClientConnectionManager){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        return httpClientBuilder;
    }

    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder(@Value("${http.request.connectionRequestTimeout}")String connectionRequestTimeout,
                                            @Value("${http.request.connectTimeout}")String connectTimeout,
                                            @Value("${http.request.socketTimeout}")String socketTimeout){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(Integer.valueOf(connectTimeout))
                .setConnectionRequestTimeout(Integer.valueOf(connectionRequestTimeout))
                .setSocketTimeout(Integer.valueOf(socketTimeout));
    }

    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder){
        return builder.build();
    }

    @Bean
    public CloseableHttpClient getHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

}
