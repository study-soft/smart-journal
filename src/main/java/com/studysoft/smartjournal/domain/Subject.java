package com.studysoft.smartjournal.domain;

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

    @ManyToMany
    @JoinTable(name = "groups_subjects",
        joinColumns = @JoinColumn(name = "subjects_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "groups_id", referencedColumnName = "id"))
    private Set<Group> groups = new HashSet<>();

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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        sb.append("}");
        return sb.toString();
    }
}
