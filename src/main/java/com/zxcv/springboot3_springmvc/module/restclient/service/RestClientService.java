package com.zxcv.springboot3_springmvc.module.restclient.service;

import com.google.gson.Gson;
import com.zxcv.springboot3_springmvc.module.restclient.dto.PostTestRequestDto;
import com.zxcv.springboot3_springmvc.module.restclient.dto.GetTestResponseDto;
import com.zxcv.springboot3_springmvc.module.restclient.dto.PostTestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestClientService {

    @Value("${zxcv1.get-test-url}")
    private String getTestUrl;
    @Value("${zxcv1.post-test-url}")
    private String postTestUrl;

    private final RestClient restClient;
    private final Gson gson;

    public String getTest() {
        log.info("getTest....");
        try {
            var response = httpGet(getTestUrl, new HttpHeaders(), GetTestResponseDto.class);
            log.info("Response : {}", response);

            return "Success";
        } catch (Exception e) {
            log.error("getTest error, exception : ", e);
            return "Failed";
        }
    }

    public String postTest(PostTestRequestDto postTestRequestDto) {
        log.info("postTest, postTestRequestDto : {}", gson.toJson(postTestRequestDto));
        try {
            var response = httpPost(postTestUrl, new HttpHeaders(), postTestRequestDto, PostTestResponseDto.class);
            log.info("Response status : {}", response.getStatus());

            return "Success";
        } catch (Exception e) {
            log.error("postTest error, exception : ", e);
            return "Failed";
        }
    }

    private <T> T httpGet(String url, HttpHeaders httpHeaders, Class<T> responseType) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<T> responseEntity = restClient.get()
                .uri(url)
                .headers(headers -> headers.addAll(httpHeaders))
                .retrieve()
                .toEntity(responseType);

        return responseEntity.getBody();
    }

    public <T> T httpPost(String url, HttpHeaders httpHeaders, Object request, Class<T> responseType) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<T> responseEntity = restClient.post()
                .uri(url)
                .headers(header -> header.addAll(httpHeaders))
                .body(request)
                .retrieve()
                .toEntity(responseType);

        return responseEntity.getBody();
    }

}
