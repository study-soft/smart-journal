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

    @Query("select b.id, b.name, b.description, b.totalScore, b.createdBy, b.created, b.updatedBy, b.updated" +
        " from Board b where b.user.login = ?#{principal.username}")
    List<Object[]> findAllByUserIsCurrentUser();

    @Query("select board from Board board where board.id =:id")
    Optional<Board> findByIdEager(@Param("id") Long id);

}
