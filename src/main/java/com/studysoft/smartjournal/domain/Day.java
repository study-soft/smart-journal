package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Day.
 */
@Entity
@Table(name = "days")
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daysSeq")
    @SequenceGenerator(name = "daysSeq", sequenceName = "days_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "result")
    private Double result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Student student;

    @OneToOne()    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"createdBy", "created", "updatedBy", "updated"})
    private DayType dayType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Day date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getResult() {
        return result;
    }

    public Day result(Double result) {
        this.result = result;
        return this;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Student getStudent() {
        return student;
    }

    public Day student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public DayType getDayType() {
        return dayType;
    }

    public Day dayType(DayType dayType) {
        this.dayType = dayType;
        return this;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Day day = (Day) o;
        if (day.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), day.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", date='").append(date).append("'");
        sb.append(", result='").append(result).append("'");
        sb.append(", studentId='").append(student.getId()).append("'");
        sb.append(", dayTypeId='").append(dayType.getId()).append("'");
        sb.append("}");
        return sb.toString();
    }
}
