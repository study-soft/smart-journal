package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data repository for the Board entity.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.user.login = ?#{principal.username}")
    List<Board> findAllByUserIsCurrentUser();

    @Query("select b from Board b left join fetch b.students s left join fetch s.days d where b.id =:id")
    Optional<Board> findByIdEager(@Param("id") Long id);

    Optional<Board> findByGroupIdAndSubjectId(Long groupId, Long subjectId);

}
