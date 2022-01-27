package com.project.cruit.entity.stack;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("frontend")
public class FrontendStack extends Stack {
    public FrontendStack(String name, String image) {
        super(name, image);
    }

    public FrontendStack() {
    }
}
