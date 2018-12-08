package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data repository for the Board entity.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select board from Board board where board.user.login = ?#{principal.username}")
    List<Board> findAllByUserIsCurrentUser();

}
