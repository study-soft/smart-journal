package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

import com.studysoft.smartjournal.domain.enumeration.Type;

/**
 * A DayType.
 */
@Entity
@Table(name = "day_types")
public class DayType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dayTypesSeq")
    @SequenceGenerator(name = "dayTypesSeq", sequenceName = "day_types_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @NotNull
    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "expiry")
    private Integer expiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Board board;

    @OneToMany(mappedBy = "dayType", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Day> days;

    public Long getId() {
        return id;
    }

    public DayType id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public DayType type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public DayType score(Double score) {
        this.score = score;
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public DayType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public DayType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public DayType expiry(Integer expiry) {
        this.expiry = expiry;
        return this;
    }

    public void setExpiry(Integer expiry) {
        this.expiry = expiry;
    }

    public Board getBoard() {
        return board;
    }

    public DayType board(Board board) {
        this.board = board;
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Set<Day> getDays() {
        return days;
    }

    public DayType days(Set<Day> days) {
        this.days = days;
        return this;
    }

    public DayType addDay(Day day) {
        this.days.add(day);
        day.setDayType(this);
        return this;
    }

    public DayType removeDay(Day day) {
        this.days.remove(day);
        day.setDayType(null);
        return this;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DayType dayType = (DayType) o;
        if (dayType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dayType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", type='").append(type).append("'");
        sb.append(", score='").append(score).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        sb.append(", expiry='").append(expiry).append("'");
        if (board != null) {
            sb.append(", boardId='").append(board.getId()).append("'");
        }
        return sb.toString();
    }
}
