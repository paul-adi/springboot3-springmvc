package com.zxcv.springboot3_springmvc.module.restclient.controller;

import com.zxcv.springboot3_springmvc.module.restclient.dto.PostTestRequestDto;
import com.zxcv.springboot3_springmvc.module.restclient.service.RestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest-client/v1")
@RequiredArgsConstructor
public class RestClientController {

    private final RestClientService restClientService;

    @GetMapping("/get-test")
    public String getTest() {
        return restClientService.getTest();
    }

    @PostMapping("/post-test")
    public String postTest(@RequestBody PostTestRequestDto postTestRequestDto) {
        return restClientService.postTest(postTestRequestDto);
    }

}
