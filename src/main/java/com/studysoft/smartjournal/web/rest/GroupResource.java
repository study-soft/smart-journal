package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Group;
import com.studysoft.smartjournal.domain.Student;
import com.studysoft.smartjournal.domain.Subject;
import com.studysoft.smartjournal.domain.User;
import com.studysoft.smartjournal.repository.BoardRepository;
import com.studysoft.smartjournal.repository.GroupRepository;
import com.studysoft.smartjournal.repository.StudentRepository;
import com.studysoft.smartjournal.repository.UserRepository;
import com.studysoft.smartjournal.security.SecurityUtils;
import com.studysoft.smartjournal.service.GroupService;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.errors.EntityNotFoundException;
import com.studysoft.smartjournal.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing group.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final StudentRepository studentRepository;

    public GroupResource(GroupRepository groupRepository,
                         GroupService groupService,
                         StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.studentRepository = studentRepository;
    }

    /**
     * POST  /groups : Create a new group.
     *
     * @param group the group to create
     * @return the ResponseEntity with status 201 (Created) and with body the new group, or with status 400 (Bad Request) if the group has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to save group : {}", group);
        if (group.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }

        groupService.checkNameExists(group);
        groupService.setCurrentUser(group);
        Group result = groupRepository.save(group);

        return ResponseEntity.created(new URI("/api/parties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing group.
     *
     * @param group the group to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @SuppressWarnings("Duplicates")
    @PutMapping("/groups")
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to update group : {}", group);
        if (group.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Group> dbGroup = groupRepository.findById(group.getId());
        if (dbGroup.isPresent() && !dbGroup.get().getName().equals(group.getName())) {
            groupService.checkNameExists(group);
        }

        groupService.checkNameExists(group);
        groupService.setCurrentUser(group);
        Group result = groupRepository.save(group);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, group.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups of current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of parties in body
     */
    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        log.debug("REST request to get all Groups");
        return groupRepository.findAllByUserIsCurrentUser();
    }

    /**
     * GET  /groups/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @SuppressWarnings("Duplicates")
    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        log.debug("REST request to get group : {}", id);
        Group group = groupRepository.findOneEager(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
        groupService.checkCurrentUser(group);
        return ResponseEntity.ok(group);
    }

    /**
     * DELETE  /groups/:id : delete the "id" group.
     *
     * @param id the id of the group to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        log.debug("REST request to delete group : {}", id);
        Group group = groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME));
        groupService.checkCurrentUser(group);
        groupService.deleteGroup(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /groups/:id/students : get students of "id" group.
     *
     * @param id the id of the group which students to retrieve
     * @return the ResponseEntity with status 200 (OK) and the list of students in body
     */
    @GetMapping("/groups/{id}/students")
    public List<Student> getGroupStudents(@PathVariable Long id) {
        log.debug("REST request to get students of group : {}", id);
        return studentRepository.findAllByGroupId(id);
    }

    /**
     * POST  /groups/{id}/students : Create a new student.
     *
     * @param id the id of group
     * @param student the student to create
     * @return the ResponseEntity with status 201 (Created) and with body the new student, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups/{id}/students")
    public ResponseEntity<Student> createStudent(@PathVariable Long id, @Valid @RequestBody Student student) throws URISyntaxException {
        if (student.getId() != null) {
            throw new BadRequestAlertException("A new student cannot already have an ID", "student", "idexists");
        }
        Student result = studentRepository.save(student);
        return ResponseEntity.created(new URI("/api/groups/" + id +"/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("student", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups/:id/students : Updates an existing student.
     *
     * @param id the id of group
     * @param student the student to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated student,
     * or with status 400 (Bad Request) if the student is not valid,
     * or with status 500 (Internal Server Error) if the student couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups/{id}/students")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to update Student : {}", student);
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "student", "idnull");
        }
        Student result = studentRepository.save(student);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("student", student.getId().toString()))
            .body(result);
    }

    /**
     * DELETE  /groups/:id/students/:studentId : delete the "id" student.
     *
     * @param groupId the id of group
     * @param studentId the id of the student to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{groupId}/students/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
        log.debug("REST request to delete Student : {}", studentId);
        groupService.checkStudent(studentId, groupId);
        studentRepository.deleteById(studentId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, studentId.toString())).build();
    }
}
