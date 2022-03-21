package com.project.cruit.util;

import com.project.cruit.domain.stack.BackendStack;
import com.project.cruit.domain.stack.FrontendStack;
import com.project.cruit.domain.stack.Stack;
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

//@Component
@RequiredArgsConstructor
@Transactional
public class InitStackDB {

    private final StackService stackService;

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

        stackService.saveStacks(stacks);
    }
}


