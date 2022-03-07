package com.project.cruit;

import com.project.cruit.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component // db 초기화 안 해주기 위해 주석 처리
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void initStackDB() {
        initService.initStack();
        initService.sampleProject();
    }
}
