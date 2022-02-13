package com.project.cruit.domain.stack;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("design")
public class DesignStack extends Stack{
}
