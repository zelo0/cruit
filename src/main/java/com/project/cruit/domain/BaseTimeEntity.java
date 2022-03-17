package com.project.cruit.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreationTimestamp
    private Date createdAt;

}
