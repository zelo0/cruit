package com.project.cruit.entity.part;

import com.project.cruit.entity.Project;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("backend")
@NoArgsConstructor
public class BackendPart extends Part{
    public BackendPart(Project project) {
        super(project);
    }
}
