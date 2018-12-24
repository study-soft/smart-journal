package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Subject.
 */
@Entity
@Table(name = "subjects")
public class Subject extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectsSeq")
    @SequenceGenerator(name = "subjectsSeq", sequenceName = "subjects_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(name = "groups_subjects",
        joinColumns = @JoinColumn(name = "subjects_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id", referencedColumnName = "id"))
    private Set<Group> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Subject id(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Subject name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Subject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public Subject user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public Subject groups(Set<Group> parties) {
        this.groups = parties;
        return this;
    }

    public Subject addGroup(Group group) {
        this.groups.add(group);
        group.getSubjects().add(this);
        return this;
    }

    public Subject removeGroup(Group group) {
        this.groups.remove(group);
        group.getSubjects().remove(this);
        return this;
    }

    public void setGroups(Set<Group> parties) {
        this.groups = parties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subject subject = (Subject) o;
        if (subject.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subject.getId());
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
