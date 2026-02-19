package com.uminotech.hira.task;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uminotech.hira.domain.Task;
import com.uminotech.hira.domain.TaskStatus;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findTasks(Long assigneeId, TaskStatus status) {
        return taskRepository.findAll().stream()
                .filter(task -> assigneeId == null || assigneeId.equals(task.assigneeId()))
                .filter(task -> status == null || status == task.status())
                .toList();
    }

    public Task create(TaskCreateCommand command) {
        Task task = new Task(
                command.title(),
                command.description(),
                command.priority(),
                command.weight(),
                TaskStatus.TODO,
                command.dueDate(),
                command.assigneeId());
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task before = findById(id);
        Task after = new Task(
                before.id(),
                before.title(),
                before.description(),
                before.priority(),
                before.weight(),
                status,
                before.dueDate(),
                before.assigneeId(),
                before.createdAt(),
                before.updatedAt());
        return taskRepository.save(after);
    }
}
