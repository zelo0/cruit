package com.project.cruit.domain.stack;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DESIGN")
public class DesignStack extends Stack{
    public DesignStack(String name, String image) {
        super(name, image);
    }

    public DesignStack() {
    }
}
