package com.uminotech.hira.task;

import java.time.LocalDate;

import com.uminotech.hira.domain.TaskPriority;

public record TaskCreateCommand(
        String title,
        String description,
        TaskPriority priority,
        Integer weight,
        LocalDate dueDate,
        Long assigneeId) {
}
