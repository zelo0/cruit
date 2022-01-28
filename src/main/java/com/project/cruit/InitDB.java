package com.project.cruit;

import com.project.cruit.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void initStackDB() {
        initService.initStack();
        initService.sampleProject();
    }
}
