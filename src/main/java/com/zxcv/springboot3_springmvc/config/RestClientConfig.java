package com.zxcv.springboot3_springmvc.config;

import com.zxcv.springboot3_springmvc.interceptor.GeneralLoggingInterceptor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfig {

    @Value("${http.client.pool.max-total-connection}")
    private int maxTotalConnection;
    @Value("${http.client.pool.max-connection-per-route}")
    private int maxConnectionPerRoute;
    @Value("${rest.client.connect-timeout}")
    private int connectTimeout;
    @Value("${rest.client.connection-request-timeout}")
    private int connectionRequestTimeout;
    @Value("${rest.client.response-timeout}")
    private int responseTimeout;

    @Bean
    public RestClient restClient() {
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(maxTotalConnection)
                .setMaxConnPerRoute(maxConnectionPerRoute)
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                        .build())
                .useSystemProperties()
                .build();

        var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeout))
                        .setResponseTimeout(Timeout.ofMilliseconds(responseTimeout))
                        .build())
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return RestClient.builder()
                .requestFactory(new BufferingClientHttpRequestFactory(factory))
                .requestInterceptor(new GeneralLoggingInterceptor())
                .messageConverters(getMessageConverter())
                .defaultStatusHandler(
                        status -> true,
                        (request, response) -> {
                        }
                )
                .build();
    }

    private List<HttpMessageConverter<?>> getMessageConverter() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return messageConverters;
    }

}
