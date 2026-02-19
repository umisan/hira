package com.uminotech.hira.task;

import java.time.LocalDate;

import com.uminotech.hira.domain.TaskPriority;
import com.uminotech.hira.domain.TaskStatus;

public record TaskUpdateCommand(
        String title,
        String description,
        TaskPriority priority,
        Integer weight,
        TaskStatus status,
        LocalDate dueDate,
        Long assigneeId) {
}
