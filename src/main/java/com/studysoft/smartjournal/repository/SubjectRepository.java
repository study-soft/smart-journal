package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Subject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
