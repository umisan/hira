package com.uminotech.hira.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("members")
public record Member(
        @Id Long id,
        @Column("name") String name,
        @Column("color") String color,
        @Column("active") boolean active,
        @CreatedDate @Column("created_at") LocalDateTime createdAt,
        @LastModifiedDate @Column("updated_at") LocalDateTime updatedAt) {

    public Member(String name, String color) {
        this(null, name, color, true, null, null);
    }

    public Member {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }
}
