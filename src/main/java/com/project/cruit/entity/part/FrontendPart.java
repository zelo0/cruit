package com.project.cruit.entity.part;

import com.project.cruit.entity.Position;
import com.project.cruit.entity.Project;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("frontend")
@NoArgsConstructor
public class FrontendPart extends Part {
    public FrontendPart(Project project) {
        super(project);
    }
}
