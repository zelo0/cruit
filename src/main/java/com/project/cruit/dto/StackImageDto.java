package com.project.cruit.dto;

import com.project.cruit.domain.stack.Stack;
import lombok.Data;

@Data
public class StackImageDto {
    private String image;

    public StackImageDto(Stack stack) {
        image = stack.getImage();
    }
}