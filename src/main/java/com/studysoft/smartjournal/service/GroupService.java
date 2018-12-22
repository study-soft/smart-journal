package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.GroupRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService {

    private static final String ENTITY_NAME = "group";

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public GroupService(UserRepository userRepository,
                        GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
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

    /**
     * Checks if there is a group with the same name for current user
     *
     * @param group group to check
     */
    public void checkNameExists(Group group) {
        Optional<Group> dbGroup = groupRepository.findByNameForCurrentUser(group.getName());
        if (dbGroup.isPresent()) {
            throw new BadRequestAlertException("Group with this name is already exists", ENTITY_NAME, "nameexists");
        }
    }
}
