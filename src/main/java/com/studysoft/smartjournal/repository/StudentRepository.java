package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Student;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
