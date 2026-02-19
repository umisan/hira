package com.uminotech.hira.web;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.uminotech.hira.domain.TaskPriority;
import com.uminotech.hira.task.TaskCreateCommand;

public class TaskCreateForm {

    @NotBlank
    private String title = "";

    private String description = "";

    private TaskPriority priority = TaskPriority.MEDIUM;

    @Min(1)
    @Max(5)
    private Integer weight = 1;

    private LocalDate dueDate;

    private Long assigneeId;

    public TaskCreateCommand toCommand() {
        return new TaskCreateCommand(title, description, priority, weight, dueDate, assigneeId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}
