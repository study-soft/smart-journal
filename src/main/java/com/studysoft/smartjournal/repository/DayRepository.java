package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Spring Data repository for the Day entity.
 */
@Repository
public interface DayRepository extends JpaRepository<Day, Long> {

    @Modifying
    @Query(value = "delete from days where date =:date and student_id =:studentId", nativeQuery = true)
    void deleteByDateAndStudentId(@Param("date") LocalDate date, @Param("studentId") Long studentId);

}
