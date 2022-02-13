package com.project.cruit.domain.stack;

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
