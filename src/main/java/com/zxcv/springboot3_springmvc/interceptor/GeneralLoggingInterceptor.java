package com.zxcv.springboot3_springmvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class GeneralLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                 ClientHttpRequestExecution execution) throws IOException {
        log.info("================== CLIENT HTTP REQUEST START ==================");
        log.info("> Request URL      : {} {}", request.getMethod(), request.getURI());
        log.info("> Request headers  : {}", request.getHeaders());
        log.info("> Request body     : {}", new String(body, StandardCharsets.UTF_8));
        var response = execution.execute(request, body);
        log.info("< Response status  : {} - {}", response.getStatusCode().value(), response.getStatusText());
        log.info("< Response headers : {}", response.getHeaders());
        log.info("< Response body    : {}", new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        log.info("================== CLIENT HTTP REQUEST FINISH =================");
        return response;
    }

}
