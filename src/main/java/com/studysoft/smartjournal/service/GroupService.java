package com.studysoft.smartjournal.service;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.Student;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.GroupRepository;
import com.studysoft.smartjournal.repository.StudentRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.studysoft.smartjournal.service.util.Constants.ENTITY_GROUP;
import static com.studysoft.smartjournal.service.util.Constants.ENTITY_STUDENT;

@Service
public class GroupService {

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final BoardRepository boardRepository;

    private final StudentRepository studentRepository;

    public GroupService(UserRepository userRepository, GroupRepository groupRepository, BoardRepository boardRepository,
                        StudentRepository studentRepository) {

        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.boardRepository = boardRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Set user from current session to field user of the group
     *
     * @param group the group to set
     */
    public void setCurrentUser(Group group) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(""))
            .orElseThrow(() -> new EntityNotFoundException("user", "no authorized user in current session"));
        group.setUser(user);
    }

    /**
     * Checks that group belongs to user from current session
     *
     * @param group the group to check
     */
    public void checkCurrentUser(Group group) {
        if (group.getUser() != null &&
            !group.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new BadRequestAlertException("Requested id does not belong to current user", ENTITY_GROUP, "accessDenied");
        }
    }

    /**
     * Checks if there is a group with the same name for current user
     *
     * @param group the group to check
     */
    public void checkNameExists(Group group) {
        Optional<Group> dbGroup = groupRepository.findByNameForCurrentUser(group.getName());
        if (dbGroup.isPresent()) {
            throw new BadRequestAlertException("Group with this name is already exists", ENTITY_GROUP, "nameexists");
        }
    }

    /**
     * Checks if the student belongs to the group
     *
     * @param studentId the id of the student to check
     * @param groupId the id of the group in which check
     */
    public void checkStudent(Long studentId, Long groupId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("student"));
        if (!student.getGroup().getId().equals(groupId)) {
            throw new BadRequestAlertException("Student with id '" + studentId + "' does not belong to current group",
                ENTITY_STUDENT, "accessDenied");
        }
    }

    /**
     * Delete group and all the boards that use this group in one transaction
     *
     * @param groupId the id of the group to delete
     */
    @Transactional
    public void deleteGroup(Long groupId) {
        boardRepository.deleteAllByGroupId(groupId);
        groupRepository.deleteById(groupId);
    }
}
