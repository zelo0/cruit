package com.project.cruit.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserStackTest {
//    @Autowired
//    private User user;


    public void add_stack_in_user_makeUserStack() {
        User user = new User();
        user.getUserStacks().add(new UserStack());
    }

    @Test
    public void remove_in_user_shouldRemoveInStack() {
        User user = new User();
        user.getUserStacks().add(new UserStack());
    }
}