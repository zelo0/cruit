package com.project.cruit.entity.part;

import com.project.cruit.entity.Project;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("design")
@NoArgsConstructor
public class DesignPart extends Part{
    public DesignPart(Project project) {
        super(project);
    }
}
