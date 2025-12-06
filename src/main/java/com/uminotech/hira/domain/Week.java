package com.uminotech.hira.domain;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("weeks")
public record Week(
        @Id Long id,
        @Column("start_date") LocalDate startDate,
        @Column("end_date") LocalDate endDate,
        @Column("label") String label) {

    public Week(LocalDate startDate, LocalDate endDate, String label) {
        this(null, startDate, endDate, label);
    }
}
