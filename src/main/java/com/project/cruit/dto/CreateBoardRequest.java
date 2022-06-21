package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
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
