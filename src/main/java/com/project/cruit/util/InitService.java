package com.project.cruit.util;

import com.project.cruit.domain.status.PartStatus;
import com.project.cruit.domain.Project;
import com.project.cruit.domain.User;
import com.project.cruit.domain.UserStack;
import com.project.cruit.domain.part.Part;
import com.project.cruit.domain.stack.BackendStack;
import com.project.cruit.domain.stack.FrontendStack;
import com.project.cruit.domain.stack.Stack;
import com.project.cruit.dto.JoinRequestDto;
import com.project.cruit.repository.UserRepository;
import com.project.cruit.service.PartService;
import com.project.cruit.service.ProjectService;
import com.project.cruit.service.StackService;
import com.project.cruit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component // 초기화 안 하기 위해 주석 처리
@RequiredArgsConstructor
@Transactional
public class InitService {

    private final StackService stackService;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final PartService partService;
    private final UserService userService;

    public void initStack() {
        List<Stack> stacks = new ArrayList<>();

        // frontend
        Stack react = new FrontendStack("react", "https://w7.pngwing.com/pngs/452/495/png-transparent-react-javascript-angularjs-ionic-github-text-logo-symmetry-thumbnail.png");
        stacks.add(react);
        Stack vue = new FrontendStack("vue", "https://w7.pngwing.com/pngs/595/279/png-transparent-vue-js-javascript-library-angularjs-react-vue-js-template-angle-text-thumbnail.png");
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
//        Stack rubyRails = new BackendStack("ruby on rails", "https://w7.pngwing.com/pngs/782/228/png-transparent-ruby-on-rails-rubygems-amazon-dynamodb-ruby-text-logo-ruby-thumbnail.png");
//        stacks.add(rubyRails);

        stackService.saveStacks(stacks);
    }

//    public void sampleUser() {
//
//    }


    public void sampleProject() {

        JoinRequestDto req1 = new JoinRequestDto("a@gmail.com", "12345678", "(테스트) 주니어 백엔드", "BACKEND");
        User user1 = userService.join(req1);
        user1.setProfile("https://w7.pngwing.com/pngs/193/722/png-transparent-red-and-blue-spider-man-illustration-spider-man-miles-morales-marvel-comics-spider-man-heroes-superhero-fictional-character-thumbnail.png");
        userRepository.save(user1);

        UserStack spring = new UserStack(user1, stackService.findByName("spring"));
        UserStack django = new UserStack(user1, stackService.findByName("django"));
        user1.getUserStacks().add(spring);
        user1.getUserStacks().add(django);

        JoinRequestDto req2 = new JoinRequestDto("test2@gmail.com", "1234", "(테스트) 시니어 백엔드", "BACKEND");
        User user2 = userService.join(req2);
        user2.setProfile("https://w7.pngwing.com/pngs/549/240/png-transparent-marvel-iron-man-iron-man-hulk-spider-man-ultron-ironman-avengers-heroes-superhero-thumbnail.png");
        userRepository.save(user2);
        JoinRequestDto req3 = new JoinRequestDto("test3@gmail.com", "1234", "(테스트) 리액트 개발자", "FRONTEND");
        User user3 = userService.join(req3);
        user3.setProfile("https://w7.pngwing.com/pngs/732/154/png-transparent-pokemon-meowth-whiskers-meowth-pokemon-go-ash-ketchum-pokemon-go-mammal-cat-like-mammal-carnivoran-thumbnail.png");
        userRepository.save(user3);

        for(int i=0; i<50; i++) {
            Project project = new Project(user1, "test " + i, "test");
            projectService.saveProject(project);
            Part backendPart = partService.getBackendPart(project);
            backendPart.addStack(stackService.findByName("django"));
        }
        Project project = new Project(user1, "test 프로젝트1", "test 프로젝트입니다. 테스트");
        projectService.saveProject(project);
        Part backendPart = partService.getBackendPart(project);
        backendPart.addMember(user2);
        backendPart.addStack(stackService.findByName("spring"));
        backendPart.addStack(stackService.findByName("hibernate"));
        partService.setStatus(backendPart, PartStatus.COMPLETED);
        Part frontendPart = partService.getFrontendPart(project);
        frontendPart.addMember(user3);
        frontendPart.addStack(stackService.findByName("react"));

        Project project2 = new Project(user3, "test 프로젝트2", "test 2번째입니다.");
        projectService.saveProject(project2);
        Part frontendPart1 = partService.getFrontendPart(project2);
        frontendPart1.addStack(stackService.findByName("vue.js"));
    }
}
