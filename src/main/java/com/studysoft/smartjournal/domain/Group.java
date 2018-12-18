package com.studysoft.smartjournal.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Group.
 */
@Entity
@Table(name = "_groups")
public class Group extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groupsSeq")
    @SequenceGenerator(name = "groupsSeq", sequenceName = "_groups_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @OrderBy("last_name, first_name ASC")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    private Set<Subject> subjects = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Group description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public Group user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Group students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Group addStudent(Student student) {
        this.students.add(student);
        student.setGroup(this);
        return this;
    }

    public Group removeStudent(Student student) {
        this.students.remove(student);
        student.setGroup(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Group subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Group addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getGroups().add(this);
        return this;
    }

    public Group removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getGroups().remove(this);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        if (group.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        if (user != null) {
            sb.append(", userId='").append(user.getId()).append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}
