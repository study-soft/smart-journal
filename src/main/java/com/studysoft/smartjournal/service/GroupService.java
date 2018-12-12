package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final UserRepository userRepository;

    public GroupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Set user from current session to field user of group
     *
     * @param group board to set
     */
    public void setCurrentUser(Group group) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException("user", "no authorized user in current session"));
        group.setUser(user);
    }
}
