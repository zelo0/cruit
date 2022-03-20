package com.project.cruit.dto;

import com.project.cruit.domain.Board;
import lombok.Data;

@Data
public class BoardContentDto {
    private String title;
    private String content;

    public BoardContentDto(Board board) {
        title = board.getTitle();
        content = board.getContent();
    }
}
