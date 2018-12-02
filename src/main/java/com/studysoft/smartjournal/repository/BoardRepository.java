package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Board;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Board entity.
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
