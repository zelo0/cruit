package com.project.cruit.domain.part;

import com.project.cruit.domain.Project;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FRONTEND")
@NoArgsConstructor
public class FrontendPart extends Part {
    public FrontendPart(Project project) {
        super(project);
    }
}
