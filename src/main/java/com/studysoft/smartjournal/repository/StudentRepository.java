package com.studysoft.smartjournal.repository;

import com.studysoft.smartjournal.domain.Student;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Student entity.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.group.user.login = ?#{principal.username}")
    List<Student> findAllByUserIsCurrentUser();

    @Query("select s from Student s where s.group.id =:groupId")
    List<Student> findAllByGroupId(@Param("groupId") Long groupId);

}
