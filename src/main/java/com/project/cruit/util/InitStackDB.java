package com.project.cruit.util;

import com.project.cruit.CommonVariables;
import com.project.cruit.domain.stack.BackendStack;
import com.project.cruit.domain.stack.DesignStack;
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
        Stack react = new FrontendStack("react", CommonVariables.awsS3UrlPrefix + "/techStacks/react.png");
        stacks.add(react);
        Stack vue = new FrontendStack("vue", CommonVariables.awsS3UrlPrefix + "/techStacks/vue.png");
        stacks.add(vue);

        // backend
        Stack spring = new BackendStack("spring", CommonVariables.awsS3UrlPrefix + "/techStacks/spring.png");
        stacks.add(spring);
        Stack django = new BackendStack("django", CommonVariables.awsS3UrlPrefix + "/techStacks/django.png");
        stacks.add(django);
        Stack flask = new BackendStack("flask", CommonVariables.awsS3UrlPrefix + "/techStacks/flask.png");
        stacks.add(flask);
        Stack express = new BackendStack("express", CommonVariables.awsS3UrlPrefix + "/techStacks/express.png");
        stacks.add(express);

        // design
        Stack figma = new DesignStack("figma", CommonVariables.awsS3UrlPrefix + "/techStacks/figma.png");
        stacks.add(figma);

        stackService.saveStacks(stacks);
    }
}


