package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Subject;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    private final UserRepository userRepository;

    public SubjectService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
