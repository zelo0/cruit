package com.project.cruit.controller;

import com.project.cruit.entity.stack.Stack;
import com.project.cruit.repository.StackRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StackApiController {
    private final StackRepository stackRepository;

    @GetMapping("/api/stacks")
    public ResponseWrapper stacks() {
        List<StackDto> response = stackRepository.findAll().stream().map(StackDto::new).collect(Collectors.toList());
        return new ResponseWrapper(response);
    }

    @Data
    static class StackDto {
        private String name;
        private String image;

        public StackDto(Stack s) {
            name = s.getName();
            image = s.getImage();
        }
    }
}
