package com.zxcv.springboot3_springmvc.module.restclient.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxcv.springboot3_springmvc.module.restclient.payload.GetTestResponse;
import com.zxcv.springboot3_springmvc.module.restclient.payload.PostTestRequest;
import com.zxcv.springboot3_springmvc.module.restclient.payload.PostTestResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestClientService {

    @Value("${zxcv1.get-test-url}")
    private String getTestUrl;
    @Value("${zxcv1.post-test-url}")
    private String postTestUrl;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public String getTest() {
        log.info("getTest....");
        try {
            var response = httpGet(getTestUrl, new HttpHeaders(), new TypeReference<GetTestResponse>() {
            });
            log.info("Response : {}", response);

            return "Success";
        } catch (Exception e) {
            log.error("getTest error, exception : ", e);
            return "Failed";
        }
    }

    public String postTest(PostTestRequest postTestRequest) {
        log.info("postTest....");
        try {
            var httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            var response = httpPost(postTestUrl, httpHeaders, postTestRequest, new TypeReference<PostTestResponse>() {
            });
            log.info("Response status : {}", response.status());

            return "Success";
        } catch (Exception e) {
            log.error("postTest error, exception : ", e);
            return "Failed";
        }
    }

    @SneakyThrows
    public <T> T httpGet(String url, HttpHeaders httpHeaders, TypeReference<T> responseType) {
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<Object> responseEntity = restClient.get()
                .uri(url)
                .headers(headers -> headers.addAll(httpHeaders))
                .retrieve()
                .toEntity(Object.class);

        return objectMapper.convertValue(responseEntity.getBody(), responseType);
    }

    @SneakyThrows
    public <T> T httpPost(String url, HttpHeaders httpHeaders, Object requestBody, TypeReference<T> responseType) {
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<Object> responseEntity = restClient.post()
                .uri(url)
                .headers(header -> header.addAll(httpHeaders))
                .body(requestBody)
                .retrieve()
                .toEntity(Object.class);

        return objectMapper.convertValue(responseEntity.getBody(), responseType);
    }

}
