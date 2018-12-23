package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.DayType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the DayType entity.
 */
@Repository
public interface DayTypeRepository extends JpaRepository<DayType, Long> {

    public List<DayType> findAllByBoardId(Long boardId);

}
