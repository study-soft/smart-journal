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

    @Query("select s from Subject s left join fetch s.groups where s.id =:id")
    Optional<Subject> findOneEager(@Param("id") Long id);

    @Query("select distinct s from Subject s where s.user.login = ?#{principal.username}")
    List<Subject> findAllByUserIsCurrentUser();

    @Query("select s from Subject s where s.name =:name and s.user.login = ?#{principal.username}")
    Optional<Subject> findByNameForCurrentUser(@Param("name") String name);
}
