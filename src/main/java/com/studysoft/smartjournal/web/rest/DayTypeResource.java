package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.DayType;
import com.studysoft.smartjournal.repository.DayTypeRepository;
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
 * REST controller for managing DayType.
 */
@RestController
@RequestMapping("/api")
public class DayTypeResource {

    private final Logger log = LoggerFactory.getLogger(DayTypeResource.class);

    private static final String ENTITY_NAME = "dayType";

    private final DayTypeRepository dayTypeRepository;

    public DayTypeResource(DayTypeRepository dayTypeRepository) {
        this.dayTypeRepository = dayTypeRepository;
    }

    /**
     * GET  /day-types/:id : get the "id" dayType.
     *
     * @param id the id of the dayType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dayType, or with status 404 (Not Found)
     */
    @GetMapping("/day-types/{id}")
    public ResponseEntity<DayType> getDayType(@PathVariable Long id) {
        log.debug("REST request to get DayType : {}", id);
        Optional<DayType> dayType = dayTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dayType);
    }
}
