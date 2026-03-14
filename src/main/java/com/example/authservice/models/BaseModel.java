package com.example.authservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @CreatedDate
    private Date CreatedAt;

    @LastModifiedDate
    private Date lastModifiedAt;
}