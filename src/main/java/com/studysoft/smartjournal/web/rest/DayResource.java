package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Day;
import com.studysoft.smartjournal.repository.DayRepository;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller for managing Day.
 */
@RestController
@RequestMapping("/api")
public class DayResource {

    private final Logger log = LoggerFactory.getLogger(DayResource.class);

    private final DayRepository dayRepository;

    public DayResource(DayRepository dayRepository) {
        this.dayRepository = dayRepository;
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
}
