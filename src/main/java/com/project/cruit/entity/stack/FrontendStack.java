package com.project.cruit.entity.stack;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("frontend")
public class FrontendStack extends Stack{
}
