package com.uminotech.hira.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uminotech.hira.domain.Task;
import com.uminotech.hira.domain.TaskPriority;
import com.uminotech.hira.domain.TaskStatus;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Test
    void shouldFilterTasksByAssigneeAndStatus() {
        TaskService service = new TaskService(taskRepository);
        Task todoForAssignee = task(1L, TaskStatus.TODO, 10L);
        Task doneForAssignee = task(2L, TaskStatus.DONE, 10L);
        Task todoForAnother = task(3L, TaskStatus.TODO, 20L);
        when(taskRepository.findAll()).thenReturn(List.of(todoForAssignee, doneForAssignee, todoForAnother));

        List<Task> actual = service.findTasks(10L, TaskStatus.TODO);

        assertThat(actual).containsExactly(todoForAssignee);
    }

    @Test
    void shouldCreateTaskWithGivenCommand() {
        TaskService service = new TaskService(taskRepository);
        TaskCreateCommand command = new TaskCreateCommand(
                "buy milk",
                "from nearby shop",
                TaskPriority.HIGH,
                3,
                LocalDate.of(2026, 2, 20),
                10L);
        Task saved = new Task(
                99L,
                command.title(),
                command.description(),
                command.priority(),
                command.weight(),
                TaskStatus.TODO,
                command.dueDate(),
                command.assigneeId(),
                LocalDateTime.of(2026, 2, 17, 0, 0),
                LocalDateTime.of(2026, 2, 17, 0, 0));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task actual = service.create(command);

        assertThat(actual.id()).isEqualTo(99L);
        assertThat(actual.title()).isEqualTo("buy milk");
        assertThat(actual.status()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskService service = new TaskService(taskRepository);
        Task before = task(10L, TaskStatus.TODO, 1L);
        when(taskRepository.findById(10L)).thenReturn(Optional.of(before));
        Task after = new Task(
                before.id(),
                before.title(),
                before.description(),
                before.priority(),
                before.weight(),
                TaskStatus.DONE,
                before.dueDate(),
                before.assigneeId(),
                before.createdAt(),
                LocalDateTime.of(2026, 2, 17, 0, 0));
        when(taskRepository.save(after)).thenReturn(after);

        Task actual = service.updateStatus(10L, TaskStatus.DONE);

        assertThat(actual.status()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository).save(after);
    }

    @Test
    void shouldUpdateTaskFields() {
        TaskService service = new TaskService(taskRepository);
        Task before = task(10L, TaskStatus.TODO, 1L);
        when(taskRepository.findById(10L)).thenReturn(Optional.of(before));
        TaskUpdateCommand command = new TaskUpdateCommand(
                "updated-title",
                "updated-desc",
                TaskPriority.HIGH,
                5,
                TaskStatus.IN_PROGRESS,
                LocalDate.of(2026, 3, 1),
                2L);
        Task after = new Task(
                before.id(),
                command.title(),
                command.description(),
                command.priority(),
                command.weight(),
                command.status(),
                command.dueDate(),
                command.assigneeId(),
                before.createdAt(),
                before.updatedAt());
        when(taskRepository.save(after)).thenReturn(after);

        Task actual = service.update(10L, command);

        assertThat(actual.title()).isEqualTo("updated-title");
        assertThat(actual.status()).isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository).save(after);
    }

    @Test
    void shouldDeleteTask() {
        TaskService service = new TaskService(taskRepository);
        Task before = task(10L, TaskStatus.TODO, 1L);
        when(taskRepository.findById(10L)).thenReturn(Optional.of(before));

        service.delete(10L);

        verify(taskRepository).delete(before);
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        TaskService service = new TaskService(taskRepository);
        when(taskRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(404L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("404");
    }

    private static Task task(Long id, TaskStatus status, Long assigneeId) {
        return new Task(
                id,
                "task-" + id,
                "desc",
                TaskPriority.MEDIUM,
                2,
                status,
                null,
                assigneeId,
                LocalDateTime.of(2026, 2, 17, 0, 0),
                LocalDateTime.of(2026, 2, 17, 0, 0));
    }
}
