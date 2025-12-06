package com.uminotech.hira.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tasks")
public record Task(
        @Id Long id,
        @Column("title") String title,
        @Column("description") String description,
        @Column("priority") TaskPriority priority,
        @Column("weight") Integer weight,
        @Column("status") TaskStatus status,
        @Column("due_date") LocalDate dueDate,
        @Column("assignee_id") Long assigneeId,
        @CreatedDate @Column("created_at") LocalDateTime createdAt,
        @LastModifiedDate @Column("updated_at") LocalDateTime updatedAt) {

    public Task(String title, String description, TaskPriority priority, Integer weight, TaskStatus status) {
        this(null, title, description, priority, weight, status, null, null, null, null);
    }

    public Task {
        if (priority == null) {
            priority = TaskPriority.MEDIUM;
        }
        if (weight == null) {
            weight = 1;
        }
        if (status == null) {
            status = TaskStatus.TODO;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }
}
