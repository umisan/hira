package com.uminotech.hira.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("week_tasks")
public class WeekTask {

    @Id
    private Long id;

    @Column("week_id")
    private Long weekId;

    @Column("task_id")
    private Long taskId;

    @Column("carried_over")
    private boolean carriedOver = false;

    @Column("planned_weight")
    private Integer plannedWeight;

    public WeekTask() {
    }

    public WeekTask(Long weekId, Long taskId) {
        this.weekId = weekId;
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public Long getWeekId() {
        return weekId;
    }

    public void setWeekId(Long weekId) {
        this.weekId = weekId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public boolean isCarriedOver() {
        return carriedOver;
    }

    public void setCarriedOver(boolean carriedOver) {
        this.carriedOver = carriedOver;
    }

    public Integer getPlannedWeight() {
        return plannedWeight;
    }

    public void setPlannedWeight(Integer plannedWeight) {
        this.plannedWeight = plannedWeight;
    }
}
