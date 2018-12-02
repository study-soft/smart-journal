package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.domain.Party;
import com.studysoft.smartjournal.repository.PartyRepository;
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
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class PartyResource {

    private final Logger log = LoggerFactory.getLogger(PartyResource.class);

    private static final String ENTITY_NAME = "group";

    private final PartyRepository partyRepository;

    public PartyResource(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    /**
     * POST  /groups : Create a new group.
     *
     * @param party the group to create
     * @return the ResponseEntity with status 201 (Created) and with body the new group, or with status 400 (Bad Request) if the group has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parties")
    public ResponseEntity<Party> createParty(@Valid @RequestBody Party party) throws URISyntaxException {
        log.debug("REST request to save Group : {}", party);
        if (party.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Party result = partyRepository.save(party);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groups : Updates an existing group.
     *
     * @param party the group to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parties")
    public ResponseEntity<Party> updateParty(@Valid @RequestBody Party party) throws URISyntaxException {
        log.debug("REST request to update Group : {}", party);
        if (party.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Party result = partyRepository.save(party);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, party.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/parties")
    public List<Party> getAllParties(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Parties");
        return partyRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /groups/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/parties/{id}")
    public ResponseEntity<Party> getParty(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<Party> party = partyRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(party);
    }

    /**
     * DELETE  /groups/:id : delete the "id" group.
     *
     * @param id the id of the group to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parties/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);

        partyRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
