package com.uminotech.hira.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "week_tasks")
public class WeekTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false)
    private boolean carriedOver = false;

    private Integer plannedWeight;

    public WeekTask() {
    }

    public WeekTask(Week week, Task task) {
        this.week = week;
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
