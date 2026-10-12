package com.zxcv.springboot3_springmvc.module.springretry.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zxcv.springboot3_springmvc.module.restclient.payload.GetTestResponse;
import com.zxcv.springboot3_springmvc.module.restclient.service.RestClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringRetryService {

    private final RestClientService restClientService;

    @Retryable(
            retryFor = {
                    HttpHostConnectException.class,
                    SocketTimeoutException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 1.5)
    )
    public String retryTest() {
        log.info("retryTest....");
        var url = "http://localhost:8081/retry-test";
        var getTestResponse = restClientService.httpGet(url, new HttpHeaders(), new TypeReference<GetTestResponse>() {
        });

        return getTestResponse.status();
    }

    @Recover
    public String recover(RestClientException e) {
        log.error("Recover from RestClientException. Exception message: {}", e.getMessage());
        return "FAILED";
    }

}
