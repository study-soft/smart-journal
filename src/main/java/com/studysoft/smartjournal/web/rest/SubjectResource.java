package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Subject;
import com.studysoft.smartjournal.repository.SubjectRepository;
import com.studysoft.smartjournal.service.SubjectService;
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

import static com.studysoft.smartjournal.service.util.Constants.ENTITY_SUBJECT;

/**
 * REST controller for managing Subject.
 */
@RestController
@RequestMapping("/api")
public class SubjectResource {

    private final Logger log = LoggerFactory.getLogger(SubjectResource.class);

    private final SubjectRepository subjectRepository;

    private final SubjectService subjectService;

    public SubjectResource(SubjectRepository subjectRepository, SubjectService subjectService) {

        this.subjectRepository = subjectRepository;
        this.subjectService = subjectService;
    }

    /**
     * POST  /subjects : Create a new subject.
     *
     * @param subject the subject to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subject, or with status 400 (Bad Request) if the subject has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) throws URISyntaxException {
        log.debug("REST request to save Subject : {}", subject);
        if (subject.getId() != null) {
            throw new BadRequestAlertException("A new subject cannot already have an ID", ENTITY_SUBJECT, "idexists");
        }

        subjectService.checkNameExists(subject);
        subjectService.setCurrentUser(subject);
        Subject result = subjectRepository.save(subject);

        return ResponseEntity.created(new URI("/api/subjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_SUBJECT, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subjects : Updates an existing subject.
     *
     * @param subject the subject to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subject,
     * or with status 400 (Bad Request) if the subject is not valid,
     * or with status 500 (Internal Server Error) if the subject couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @SuppressWarnings("Duplicates")
    @PutMapping("/subjects")
    public ResponseEntity<Subject> updateSubject(@Valid @RequestBody Subject subject) throws URISyntaxException {
        log.debug("REST request to update Subject : {}", subject);
        if (subject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_SUBJECT, "idnull");
        }

        Optional<Subject> dbSubject = subjectRepository.findById(subject.getId());
        if (dbSubject.isPresent() && !dbSubject.get().getName().equals(subject.getName())) {
            subjectService.checkNameExists(subject);
        }

        subjectService.setCurrentUser(subject);
        Subject result = subjectRepository.save(subject);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_SUBJECT, subject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subjects : get all the subjects of current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subjects in body
     */
    @GetMapping("/subjects")
    public List<Subject> getAllSubjects() {
        log.debug("REST request to get all Subjects");
        return subjectRepository.findAllByUserIsCurrentUser();
    }

    /**
     * GET  /subjects/:id : get the "id" subject.
     *
     * @param id the id of the subject to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subject, or with status 404 (Not Found)
     */
    @SuppressWarnings("Duplicates")
    @GetMapping("/subjects/{id}")
    public ResponseEntity<?> getSubject(@PathVariable Long id) {
        log.debug("REST request to get Subject : {}", id);
        Subject subject = subjectRepository.findOneEager(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_SUBJECT));
        subjectService.checkCurrentUser(subject);
        return ResponseEntity.ok(subject);
    }

    /**
     * DELETE  /subjects/:id : delete the "id" subject.
     *
     * @param id the id of the subject to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/subjects/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        log.debug("REST request to delete Subject : {}", id);
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_SUBJECT));
        subjectService.checkCurrentUser(subject);
        subjectService.deleteSubject(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_SUBJECT, id.toString())).build();
    }
}
