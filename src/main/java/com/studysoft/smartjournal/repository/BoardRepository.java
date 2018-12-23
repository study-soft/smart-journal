package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Board;
import com.studysoft.smartjournal.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data repository for the Board entity.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.user.login = ?#{principal.username}")
    List<Board> findAllByUserIsCurrentUser();

    @Query("select b from Board b left join fetch b.group g left join fetch g.students s " +
        "left join fetch s.days d left join fetch b.dayTypes dt where b.id =:id")
    Optional<Board> findByIdEager(@Param("id") Long id);

    @Query("select b from Board b where b.title =:title and b.user.login = ?#{principal.username}")
    Optional<Board> findByTitleForCurrentUser(@Param("title") String title);

    @Modifying
    @Query(value = "insert into groups_subjects(groups_id, subjects_id) values (:groupId, :subjectId)", nativeQuery = true)
    void fillGroupsSubjects(@Param("groupId") Long groupId, @Param("subjectId") Long subjectId);

    @Modifying
    @Query(value = "delete from groups_subjects where groups_id =:groupId and subjects_id=:subjectId", nativeQuery = true)
    void deleteGroupsSubjects(@Param("groupId") Long groupId, @Param("subjectId") Long subjectId);

    void deleteAllByGroupId(Long groupId);
}
