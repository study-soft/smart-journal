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

    @Query(value = "select distinct g from Group g left join fetch g.subjects",
        countQuery = "select count(distinct g) from Group g")
    Page<Group> findAllEager(Pageable pageable);

    @Query(value = "select distinct g from Group g left join fetch g.subjects")
    List<Group> findAllEager();

    @Query("select g from Group g left join fetch g.subjects where g.id =:id")
    Optional<Group> findOneEager(@Param("id") Long id);

    @Query("select distinct g from Group g where g.user.login = ?#{principal.username}")
    List<Group> findAllByUserIsCurrentUser();

    @Query("select g from Group g where g.name =:name and g.user.login = ?#{principal.username}")
    Optional<Group> findByNameForCurrentUser(@Param("name") String name);

}
