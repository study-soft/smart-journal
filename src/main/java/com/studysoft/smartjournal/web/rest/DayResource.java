package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Day;
import com.studysoft.smartjournal.repository.DayRepository;
import com.studysoft.smartjournal.web.rest.errors.BadRequestAlertException;
import com.studysoft.smartjournal.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
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
 * REST controller for managing Day.
 */
@RestController
@RequestMapping("/api")
public class DayResource {

    private final Logger log = LoggerFactory.getLogger(DayResource.class);

    private static final String ENTITY_NAME = "day";

    private final DayRepository dayRepository;

    public DayResource(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
    }

    /**
     * POST  /days : Create a new day.
     *
     * @param day the day to create
     * @return the ResponseEntity with status 201 (Created) and with body the new day, or with status 400 (Bad Request) if the day has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/days")
    public ResponseEntity<Day> createDay(@Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to save Day : {}", day);
        if (day.getId() != null) {
            throw new BadRequestAlertException("A new day cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Day result = dayRepository.save(day);
        return ResponseEntity.created(new URI("/api/days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /days : Updates an existing day.
     *
     * @param day the day to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated day,
     * or with status 400 (Bad Request) if the day is not valid,
     * or with status 500 (Internal Server Error) if the day couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/days")
    public ResponseEntity<Day> updateDay(@Valid @RequestBody Day day) throws URISyntaxException {
        log.debug("REST request to update Day : {}", day);
        if (day.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Day result = dayRepository.save(day);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, day.getId().toString()))
            .body(result);
    }

    /**
     * GET  /days : get all the days.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of days in body
     */
    @GetMapping("/days")
    public List<Day> getAllDays() {
        log.debug("REST request to get all Days");
        return dayRepository.findAll();
    }

    /**
     * GET  /days/:id : get the "id" day.
     *
     * @param id the id of the day to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the day, or with status 404 (Not Found)
     */
    @GetMapping("/days/{id}")
    public ResponseEntity<Day> getDay(@PathVariable Long id) {
        log.debug("REST request to get Day : {}", id);
        Optional<Day> day = dayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(day);
    }

    /**
     * DELETE  /days/:id : delete the "id" day.
     *
     * @param id the id of the day to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/days/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        log.debug("REST request to delete Day : {}", id);

        dayRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
