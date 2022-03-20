package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequest {
    @NotNull
    private Long projectId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
