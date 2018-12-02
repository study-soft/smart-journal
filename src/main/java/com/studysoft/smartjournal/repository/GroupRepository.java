package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Group entity.
 */
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(value = "select distinct party from Group party left join fetch party.subjects",
        countQuery = "select count(distinct party) from Group party")
    Page<Group> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct party from Group party left join fetch party.subjects")
    List<Group> findAllWithEagerRelationships();

    @Query("select party from Group party left join fetch party.subjects where party.id =:id")
    Optional<Group> findOneWithEagerRelationships(@Param("id") Long id);

}
