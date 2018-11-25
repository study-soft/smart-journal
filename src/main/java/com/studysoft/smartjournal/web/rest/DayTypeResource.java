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
     * POST  /day-types : Create a new dayType.
     *
     * @param dayType the dayType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dayType, or with status 400 (Bad Request) if the dayType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/day-types")
    public ResponseEntity<DayType> createDayType(@Valid @RequestBody DayType dayType) throws URISyntaxException {
        log.debug("REST request to save DayType : {}", dayType);
        if (dayType.getId() != null) {
            throw new BadRequestAlertException("A new dayType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DayType result = dayTypeRepository.save(dayType);
        return ResponseEntity.created(new URI("/api/day-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /day-types : Updates an existing dayType.
     *
     * @param dayType the dayType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dayType,
     * or with status 400 (Bad Request) if the dayType is not valid,
     * or with status 500 (Internal Server Error) if the dayType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/day-types")
    public ResponseEntity<DayType> updateDayType(@Valid @RequestBody DayType dayType) throws URISyntaxException {
        log.debug("REST request to update DayType : {}", dayType);
        if (dayType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DayType result = dayTypeRepository.save(dayType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dayType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /day-types : get all the dayTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dayTypes in body
     */
    @GetMapping("/day-types")
    public List<DayType> getAllDayTypes() {
        log.debug("REST request to get all DayTypes");
        return dayTypeRepository.findAll();
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

    /**
     * DELETE  /day-types/:id : delete the "id" dayType.
     *
     * @param id the id of the dayType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/day-types/{id}")
    public ResponseEntity<Void> deleteDayType(@PathVariable Long id) {
        log.debug("REST request to delete DayType : {}", id);

        dayTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
