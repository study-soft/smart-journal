package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Board.
 */
@Entity
@Table(name = "boards")
public class Board extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boardsSeq")
    @SequenceGenerator(name = "boardsSeq", sequenceName = "boards_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "total_score")
    private Double totalScore;

    @OneToOne    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"createdBy", "created", "updatedBy", "updated"})
    private Group group;

    @OneToOne    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"createdBy", "created", "updatedBy", "updated"})
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private Set<DayType> dayTypes = new HashSet<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

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

    public Double getTotalScore() {
        return totalScore;
    }

    public Board totalScore(Double totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Group getGroup() {
        return group;
    }

    public Board party(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
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

    public User getUser() {
        return user;
    }

    public Board user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        StringBuilder sb = new StringBuilder(getClass() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        sb.append(", totalScore='").append(totalScore).append("'");
        sb.append(", groupId='").append(group.getId()).append("'");
        sb.append(", subjectId='").append(subject.getId()).append("'");
        sb.append(", userId='").append(user.getId()).append("'");
        sb.append("}");
        return sb.toString();
    }
}
