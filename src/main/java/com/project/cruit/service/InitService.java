package com.project.cruit.service;

import com.project.cruit.entity.Position;
import com.project.cruit.entity.Project;
import com.project.cruit.entity.User;
import com.project.cruit.entity.stack.BackendStack;
import com.project.cruit.entity.stack.FrontendStack;
import com.project.cruit.entity.stack.Stack;
import com.project.cruit.repository.ProjectRepository;
import com.project.cruit.repository.StackRepository;
import com.project.cruit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class InitService {

    private final StackService stackService;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final PartService partService;

    public void initStack() {
        List<Stack> stacks = new ArrayList<>();

        // frontend
        Stack react = new FrontendStack("react", "https://w7.pngwing.com/pngs/452/495/png-transparent-react-javascript-angularjs-ionic-github-text-logo-symmetry-thumbnail.png");
        stacks.add(react);
        Stack vue = new FrontendStack("vue.js", "https://w7.pngwing.com/pngs/595/279/png-transparent-vue-js-javascript-library-angularjs-react-vue-js-template-angle-text-thumbnail.png");
        stacks.add(vue);

        // backend
        Stack spring = new BackendStack("spring", "https://w7.pngwing.com/pngs/713/936/png-transparent-spring-framework-representational-state-transfer-java-api-for-restful-web-services-microservices-others-text-trademark-logo-thumbnail.png");
        stacks.add(spring);
        Stack hibernate = new BackendStack("hibernate", "https://w7.pngwing.com/pngs/709/504/png-transparent-hibernate-spring-framework-java-persistence-api-java-annotation-others-text-logo-map-thumbnail.png");
        stacks.add(hibernate);
        Stack django = new BackendStack("django", "https://w7.pngwing.com/pngs/159/366/png-transparent-django-python-computer-icons-logo-python-text-label-rectangle-thumbnail.png");
        stacks.add(django);
        Stack flask = new BackendStack("flask", "https://w7.pngwing.com/pngs/166/342/png-transparent-flask-python-bottle-web-framework-web-application-flask-white-monochrome-shoe-thumbnail.png");
        stacks.add(flask);
        Stack node = new BackendStack("node.js", "https://w7.pngwing.com/pngs/416/280/png-transparent-node-js-express-js-javascript-redis-mean-node-js-angle-text-service-thumbnail.png");
        stacks.add(node);
        Stack rubyRails = new BackendStack("ruby on rails", "https://w7.pngwing.com/pngs/782/228/png-transparent-ruby-on-rails-rubygems-amazon-dynamodb-ruby-text-logo-ruby-thumbnail.png");
        stacks.add(rubyRails);

        stackService.saveStacks(stacks);
    }

//    public void sampleUser() {
//
//    }


    public void sampleProject() {
        User user1 = new User("test@gmail.com", "1234", "(테스트) 주니어 백엔드", Position.BACKEND, "test@gihub.com", false);
        user1.setProfile("https://w7.pngwing.com/pngs/193/722/png-transparent-red-and-blue-spider-man-illustration-spider-man-miles-morales-marvel-comics-spider-man-heroes-superhero-fictional-character-thumbnail.png");
        userRepository.save(user1);
        User user2 = new User("test2@gmail.com", "1234", "(테스트) 시니어 백엔드", Position.BACKEND, "test2@gihub.com", true);
        user2.setProfile("https://w7.pngwing.com/pngs/549/240/png-transparent-marvel-iron-man-iron-man-hulk-spider-man-ultron-ironman-avengers-heroes-superhero-thumbnail.png");
        userRepository.save(user2);

        Project project = new Project(user1, "test 프로젝트1", "test 프로젝트입니다. 테스트");
        projectService.saveProject(project);
        partService.getBackendPart(project).addMember(user2);
    }
}
