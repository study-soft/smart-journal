package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Day.
 */
@Entity
@Table(name = "day")
public class Day implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "result")
    private Double result;

    @ManyToOne
    @JsonIgnoreProperties("days")
    private Student student;

    @OneToOne    @JoinColumn(unique = true)
    private DayType dayType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Day date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return "Day{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", result=" + getResult() +
            "}";
    }
}
