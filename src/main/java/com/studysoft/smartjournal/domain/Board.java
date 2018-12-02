package com.studysoft.smartjournal.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Board.
 */
@Entity
@Table(name = "board")
public class Board extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boardSequence")
    @SequenceGenerator(name = "boardSequence", sequenceName = "board_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne    @JoinColumn(unique = true)
    private Party party;

    @OneToOne    @JoinColumn(unique = true)
    private Subject subject;

    @OneToMany(mappedBy = "board")
    private Set<Student> students = new HashSet<>();
    @OneToMany(mappedBy = "board")
    private Set<DayType> dayTypes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Board name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Board description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Party getParty() {
        return party;
    }

    public Board party(Party party) {
        this.party = party;
        return this;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Subject getSubject() {
        return subject;
    }

    public Board subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Board students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Board addStudent(Student student) {
        this.students.add(student);
        student.setBoard(this);
        return this;
    }

    public Board removeStudent(Student student) {
        this.students.remove(student);
        student.setBoard(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<DayType> getDayTypes() {
        return dayTypes;
    }

    public Board dayTypes(Set<DayType> dayTypes) {
        this.dayTypes = dayTypes;
        return this;
    }

    public Board addDayType(DayType dayType) {
        this.dayTypes.add(dayType);
        dayType.setBoard(this);
        return this;
    }

    public Board removeDayType(DayType dayType) {
        this.dayTypes.remove(dayType);
        dayType.setBoard(null);
        return this;
    }

    public void setDayTypes(Set<DayType> dayTypes) {
        this.dayTypes = dayTypes;
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
        Board board = (Board) o;
        if (board.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), board.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Board{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
