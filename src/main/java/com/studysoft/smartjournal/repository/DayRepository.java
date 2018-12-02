package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Day;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Day entity.
 */
@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

}
