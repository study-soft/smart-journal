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

    @Query(value = "select distinct _group from Group _group left join fetch _group.subjects",
        countQuery = "select count(distinct _group) from Group _group")
    Page<Group> findAllEager(Pageable pageable);

    @Query(value = "select distinct _group from Group _group left join fetch _group.subjects")
    List<Group> findAllEager();

    @Query("select _group from Group _group left join fetch _group.subjects " +
        "where _group.id =:id")
    Optional<Group> findOneEager(@Param("id") Long id);

    @Query("select distinct _group from Group _group where _group.user.login = ?#{principal.username}")
    List<Group> findAllByUserIsCurrentUser();

}
