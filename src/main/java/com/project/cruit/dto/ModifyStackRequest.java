package com.project.cruit.dto;

import com.project.cruit.domain.stack.Stack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyStackRequest {
    private List<Stack> stacks;
}
