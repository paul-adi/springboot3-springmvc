package com.zxcv.springboot3_springmvc.module.springretry.controller;

import com.zxcv.springboot3_springmvc.module.springretry.service.SpringRetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("spring-retry/v1")
@RequiredArgsConstructor
public class SpringRetryController {

    private final SpringRetryService restClientService;

    @GetMapping("/retry-test")
    public String retryTest() {
        return restClientService.retryTest();
    }

}
