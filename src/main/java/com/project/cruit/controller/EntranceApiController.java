package com.project.cruit.controller;

import com.project.cruit.dto.ResponseWrapper;
import com.project.cruit.dto.SimpleMessageBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntranceApiController {
    // 로드밸런서의 healthy check 통과용 api
    @GetMapping("/")
    public ResponseWrapper<SimpleMessageBody> welcome() {
        return new ResponseWrapper<>(new SimpleMessageBody("server's running"));
    }
}
