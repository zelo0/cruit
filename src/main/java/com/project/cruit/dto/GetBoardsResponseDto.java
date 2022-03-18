package com.project.cruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetBoardsResponseDto {
    private List<DetailBoardDto> posts;
}
