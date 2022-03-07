package com.project.cruit.domain.part;

import com.project.cruit.domain.Project;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DESIGN")
@NoArgsConstructor
public class DesignPart extends Part{
    public DesignPart(Project project) {
        super(project);
    }
}
