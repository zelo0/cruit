package com.project.cruit.domain.stack;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("backend")
public class BackendStack extends Stack{
    public BackendStack(String name, String image) {
        super(name, image);
    }

    public BackendStack() {
    }
}
