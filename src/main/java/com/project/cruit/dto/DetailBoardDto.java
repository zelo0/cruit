package com.project.cruit.dto;

import com.project.cruit.domain.Board;
import com.project.cruit.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DetailBoardDto {
    private Long id;
    private String title;
    private String content;
    private UserNameImageDto user;

    public DetailBoardDto(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        user = new UserNameImageDto(board.getWriter());
    }
}
