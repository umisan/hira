package com.uminotech.hira.task;

import org.springframework.data.repository.ListCrudRepository;

import com.uminotech.hira.domain.Task;

public interface TaskRepository extends ListCrudRepository<Task, Long> {
}
