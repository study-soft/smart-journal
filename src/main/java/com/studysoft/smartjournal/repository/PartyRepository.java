package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Party entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    @Query(value = "select distinct party from Party party left join fetch party.subjects",
        countQuery = "select count(distinct party) from Party party")
    Page<Party> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct party from Party party left join fetch party.subjects")
    List<Party> findAllWithEagerRelationships();

    @Query("select party from Party party left join fetch party.subjects where party.id =:id")
    Optional<Party> findOneWithEagerRelationships(@Param("id") Long id);

}
