package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.DayType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DayType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DayTypeRepository extends JpaRepository<DayType, Long> {

}
