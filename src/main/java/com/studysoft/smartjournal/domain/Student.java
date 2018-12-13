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
 * A Student.
 */
@Entity
@Table(name = "students")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentsSeq")
    @SequenceGenerator(name = "studentsSeq", sequenceName = "students_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "rating")
    private Double rating;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @OrderBy("date ASC")
    private Set<Day> days = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Board board;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Student middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Double getRating() {
        return rating;
    }

    public Student rating(Double rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<Day> getDays() {
        return days;
    }

    public Student days(Set<Day> days) {
        this.days = days;
        return this;
    }

    public Student addDay(Day day) {
        this.days.add(day);
        day.setStudent(this);
        return this;
    }

    public Student removeDay(Day day) {
        this.days.remove(day);
        day.setStudent(null);
        return this;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    public Board getBoard() {
        return board;
    }

    public Student board(Board board) {
        this.board = board;
        return this;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if (student.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", firstName='").append(firstName).append("'");
        sb.append(", lastName='").append(lastName).append("'");
        sb.append(", middleName='").append(middleName).append("'");
        sb.append(", rating='").append(rating).append("'");
        if (board != null) {
            sb.append(", boardId='").append(board.getId()).append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}
