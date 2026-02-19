package com.uminotech.hira.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.uminotech.hira.domain.TaskStatus;
import com.uminotech.hira.task.MemberRepository;
import com.uminotech.hira.task.TaskNotFoundException;
import com.uminotech.hira.task.TaskService;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final MemberRepository memberRepository;

    public TaskController(TaskService taskService, MemberRepository memberRepository) {
        this.taskService = taskService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskStatus status,
            Model model) {
        model.addAttribute("tasks", taskService.findTasks(assigneeId, status));
        model.addAttribute("members", memberRepository.findAllByActiveTrue());
        model.addAttribute("selectedAssigneeId", assigneeId);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new TaskCreateForm());
        model.addAttribute("members", memberRepository.findAllByActiveTrue());
        return "tasks/new";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TaskCreateForm form) {
        taskService.create(form.toCommand());
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", TaskEditForm.from(taskService.findById(id)));
        model.addAttribute("members", memberRepository.findAllByActiveTrue());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("taskId", id);
        return "tasks/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("form") TaskEditForm form) {
        taskService.update(id, form.toCommand());
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        taskService.updateStatus(id, status);
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public void handleTaskNotFound(TaskNotFoundException exception) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
    }
}
