package com.project.cruit.repository;

import com.project.cruit.domain.Position;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.Question;
import com.project.cruit.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("parent 없는 질문만 find")
    void OnlyQuestionWithNoParent() {
        // given
        User user = testEntityManager.persist(new User("test", "test", "test", Position.FRONTEND.name()));
        Project project = testEntityManager.persist(new Project(user, "test", "test"));

        Question parentQuestion = testEntityManager.persist(new Question(user, "test", project, null));
        Question childQuestion1 = testEntityManager.persist(new Question(user, "test", project, parentQuestion));
        Question childQuestion2 = testEntityManager.persist(new Question(user, "test", project, parentQuestion));

        // when
        List<Question> foundQuestions = questionRepository.findQuestionsByProjectIdAndParentNonExists(project);

        // then
        Assertions.assertThat(foundQuestions.size()).isEqualTo(1);
    }

}