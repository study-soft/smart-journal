package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Subject entity.
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value = "select distinct subject from Subject subject left join fetch subject.groups",
        countQuery = "select count(distinct subject) from Subject subject")
    Page<Subject> findAllEager(Pageable pageable);

    @Query(value = "select distinct subject from Subject subject left join fetch subject.groups")
    List<Subject> findAllEager();

    @Query("select subject from Subject subject left join fetch subject.groups where subject.id =:id")
    Optional<Subject> findOneEager(@Param("id") Long id);

    @Query("select distinct subject from Subject subject where subject.user.login = ?#{principal.username}")
    List<Subject> findAllByUserIsCurrentUser();
}
