package com.project.cruit.dto;

import com.project.cruit.controller.ProjectApiController;
import com.project.cruit.domain.Output;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailProjectDto {
    private Long id;
    private SimpleUserInfo proposer;
    private String name;
    private String description;
    private Output output;
    private List<QuestionDto> questions;
    private List<DetailPartDto> parts;

    public DetailProjectDto(Project project, List<Question> hierarchicalQuestions) {
        id = project.getId();
        proposer = new SimpleUserInfo(project.getProposer(), false);
        name = project.getName();
        description = project.getDescription();
        questions = hierarchicalQuestions.stream().map(QuestionDto::new).collect(Collectors.toList());
        parts = project.getParts().stream().map(DetailPartDto::new).collect(Collectors.toList());
    }
}
