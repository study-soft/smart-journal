package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.GroupRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GroupService {

    private static final String ENTITY_NAME = "group";

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final BoardRepository boardRepository;

    public GroupService(UserRepository userRepository,
                        GroupRepository groupRepository,
                        BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.boardRepository = boardRepository;
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
     * Checks that group belongs to user from current session
     *
     * @param group group to check
     */
    public void checkCurrentUser(Group group) {
        if (group.getUser() != null &&
            !group.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_NAME, "accessDenied");
        }
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

    @Transactional
    public void deleteGroup(Long groupId) {
//        boardRepository.deleteByGroupId(groupId);
        groupRepository.deleteById(groupId);
    }
}
