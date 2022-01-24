package com.project.cruit.entity.stack;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("backend")
public class BackendStack extends Stack{
}
