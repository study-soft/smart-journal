package com.studysoft.smartjournal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "total_score")
    private Double totalScore;

    @OneToOne    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"createdBy", "created", "updatedBy", "updated"})
    private Subject subject;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private Set<DayType> dayTypes = new HashSet<>();

    @OneToOne    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"createdBy", "created", "updatedBy", "updated"})
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Board title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @SuppressWarnings("Duplicates")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("id='").append(id).append("'");
        sb.append(", title='").append(title).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        sb.append(", totalScore='").append(totalScore).append("'");
        if (group != null) {
            sb.append(", groupId='").append(group.getId()).append("'");
        }
        if (subject != null) {
            sb.append(", subjectId='").append(subject.getId()).append("'");
        }
        if (user != null) {
            sb.append(", userId='").append(user.getId()).append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}
