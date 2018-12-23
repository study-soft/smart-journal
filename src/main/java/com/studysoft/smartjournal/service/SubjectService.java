package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.Subject;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.SubjectRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectService {

    private static final String ENTITY_NAME = "group";

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public SubjectService(UserRepository userRepository,
                          SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Set user from current session to field user of subject
     *
     * @param subject board to set
     */
    public void setCurrentUser(Subject subject) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException("user", "no authorized user in current session"));
        subject.setUser(user);
    }

    /**
     * Checks that subject belongs to user from current session
     *
     * @param subject subject to check
     */
    public void checkCurrentUser(Subject subject) {
        if (subject.getUser() != null &&
            !subject.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_NAME, "accessDenied");
        }
    }


    /**
     * Checks if there is a subject with the same name for current user
     *
     * @param subject subject to check
     */
    public void checkNameExists(Subject subject) {
        Optional<Subject> dbSubject = subjectRepository.findByNameForCurrentUser(subject.getName());
        if (dbSubject.isPresent()) {
            throw new BadRequestAlertException("Subject with this name is already exists", ENTITY_NAME, "nameexists");
        }
    }
}
