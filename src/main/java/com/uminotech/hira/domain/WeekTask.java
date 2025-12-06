package com.uminotech.hira.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("week_tasks")
public record WeekTask(
        @Id Long id,
        @Column("week_id") Long weekId,
        @Column("task_id") Long taskId,
        @Column("carried_over") boolean carriedOver,
        @Column("planned_weight") Integer plannedWeight) {

    public WeekTask(Long weekId, Long taskId) {
        this(null, weekId, taskId, false, null);
    }
}
