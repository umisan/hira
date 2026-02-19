package com.uminotech.hira.web;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uminotech.hira.domain.Member;
import com.uminotech.hira.domain.Task;
import com.uminotech.hira.domain.TaskPriority;
import com.uminotech.hira.domain.TaskStatus;
import com.uminotech.hira.task.MemberRepository;
import com.uminotech.hira.task.TaskCreateCommand;
import com.uminotech.hira.task.TaskService;

class TaskControllerTest {

    private TaskService taskService;
    private MemberRepository memberRepository;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.taskService = Mockito.mock(TaskService.class);
        this.memberRepository = Mockito.mock(MemberRepository.class);
        TaskController controller = new TaskController(taskService, memberRepository);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldShowTaskList() throws Exception {
        when(taskService.findTasks(1L, TaskStatus.TODO)).thenReturn(List.of(task(10L)));
        when(memberRepository.findAllByActiveTrue()).thenReturn(List.of(new Member(1L, "alice", "#111111", true, null, null)));

        mockMvc.perform(get("/tasks")
                        .param("assigneeId", "1")
                        .param("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", hasSize(1)));
    }

    @Test
    void shouldShowTaskForm() throws Exception {
        when(memberRepository.findAllByActiveTrue()).thenReturn(List.of());

        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void shouldCreateTaskAndRedirectToList() throws Exception {
        when(taskService.create(new TaskCreateCommand(
                "buy milk",
                "for breakfast",
                TaskPriority.HIGH,
                3,
                null,
                1L))).thenReturn(task(50L));

        mockMvc.perform(post("/tasks")
                        .param("title", "buy milk")
                        .param("description", "for breakfast")
                        .param("priority", "HIGH")
                        .param("weight", "3")
                        .param("assigneeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void shouldShowTaskDetail() throws Exception {
        when(taskService.findById(99L)).thenReturn(task(99L));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/detail"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void shouldUpdateStatusAndRedirectToDetail() throws Exception {
        when(taskService.updateStatus(15L, TaskStatus.DONE)).thenReturn(task(15L));

        mockMvc.perform(post("/tasks/15/status")
                        .param("status", "DONE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/15"));
    }

    private static Task task(Long id) {
        return new Task(
                id,
                "task-" + id,
                "desc",
                TaskPriority.MEDIUM,
                2,
                TaskStatus.TODO,
                null,
                1L,
                LocalDateTime.of(2026, 2, 17, 0, 0),
                LocalDateTime.of(2026, 2, 17, 0, 0));
    }
}
