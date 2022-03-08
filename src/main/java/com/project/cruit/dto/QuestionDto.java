package com.project.cruit.dto;

import com.project.cruit.controller.QuestionApiController;
import com.project.cruit.domain.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private Long parentId;
    private UserAvatar questioner;
    private String content;
    private List<QuestionDto> children = new ArrayList<>();


    public QuestionDto(Question question) {
        this.id = question.getId();
        // parent가 없는 question은 parentId 값으로 -1 반환
        this.parentId = question.getParent() == null ? -1 : question.getParent().getId();
        this.questioner = new UserAvatar(question.getQuestioner());
        this.content = question.getContent();

        List<Question> children = question.getChildren();
        for (Question child : children) {
            this.children.add(new QuestionDto(child));
        }
    }
}