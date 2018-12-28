package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Subject;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.SubjectRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.studysoft.smartjournal.service.util.Constants.ENTITY_SUBJECT;
import static com.studysoft.smartjournal.service.util.Constants.ENTITY_USER;

@Service
public class SubjectService {

    private final UserRepository userRepository;

    private final SubjectRepository subjectRepository;

    private final BoardRepository boardRepository;

    public SubjectService(UserRepository userRepository, SubjectRepository subjectRepository, BoardRepository boardRepository) {

        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * Set user from current session to field user of subject
     *
     * @param subject the subject to set
     */
    public void setCurrentUser(Subject subject) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_USER, "no authorized user in current session"));
        subject.setUser(user);
    }

    /**
     * Check that subject belongs to user from current session
     *
     * @param subject the subject to check
     */
    public void checkCurrentUser(Subject subject) {
        if (subject.getUser() != null &&
            !subject.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_SUBJECT, "accessDenied");
        }
    }


    /**
     * Check if there is a subject with the same name for current user
     *
     * @param subject the subject to check
     */
    public void checkNameExists(Subject subject) {
        Optional<Subject> dbSubject = subjectRepository.findByNameForCurrentUser(subject.getName());
        if (dbSubject.isPresent()) {
            throw new BadRequestAlertException("Subject with this name is already exists", ENTITY_SUBJECT, "nameexists");
        }
    }

    /**
     * Delete the subject and all boards that use this subject in one transaction
     *
     * @param subjectId the id of the subject to delete
     */
    @Transactional
    public void deleteSubject(Long subjectId) {
        boardRepository.deleteAllBySubjectId(subjectId);
        subjectRepository.deleteById(subjectId);
    }
}
