package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.SmartjournalApp;

import com.studysoft.smartjournal.domain.Party;
import com.studysoft.smartjournal.repository.PartyRepository;
import com.studysoft.smartjournal.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static com.studysoft.smartjournal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PartyResource REST controller.
 *
 * @see PartyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartjournalApp.class)
public class PartyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PartyRepository partyRepository;

    @Mock
    private PartyRepository partyRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPartyMockMvc;

    private Party party;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartyResource partyResource = new PartyResource(partyRepository);
        this.restPartyMockMvc = MockMvcBuilders.standaloneSetup(partyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Party createEntity(EntityManager em) {
        Party party = new Party()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return party;
    }

    @Before
    public void initTest() {
        party = createEntity(em);
    }

    @Test
    @Transactional
    public void createParty() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // Create the Party
        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isCreated());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate + 1);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testParty.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testParty.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createPartyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // Create the Party with an existing ID
        party.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        party.setName(null);

        // Create the Party, which fails.

        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        party.setCreated(null);

        // Create the Party, which fails.

        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        party.setUpdated(null);

        // Create the Party, which fails.

        restPartyMockMvc.perform(post("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParties() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get all the partyList
        restPartyMockMvc.perform(get("/api/parties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(party.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPartiesWithEagerRelationshipsIsEnabled() throws Exception {
        PartyResource partyResource = new PartyResource(partyRepositoryMock);
        when(partyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPartyMockMvc = MockMvcBuilders.standaloneSetup(partyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPartyMockMvc.perform(get("/api/parties?eagerload=true"))
        .andExpect(status().isOk());

        verify(partyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPartiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        PartyResource partyResource = new PartyResource(partyRepositoryMock);
            when(partyRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPartyMockMvc = MockMvcBuilders.standaloneSetup(partyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPartyMockMvc.perform(get("/api/parties?eagerload=true"))
        .andExpect(status().isOk());

            verify(partyRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        // Get the party
        restPartyMockMvc.perform(get("/api/parties/{id}", party.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(party.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParty() throws Exception {
        // Get the party
        restPartyMockMvc.perform(get("/api/parties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party
        Party updatedParty = partyRepository.findById(party.getId()).get();
        // Disconnect from session so that the updates on updatedParty are not directly saved in db
        em.detach(updatedParty);
        updatedParty
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restPartyMockMvc.perform(put("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParty)))
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        Party testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testParty.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testParty.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Create the Party

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc.perform(put("/api/parties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(party)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(party);

        int databaseSizeBeforeDelete = partyRepository.findAll().size();

        // Get the party
        restPartyMockMvc.perform(delete("/api/parties/{id}", party.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Party> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Party.class);
        Party party1 = new Party();
        party1.setId(1L);
        Party party2 = new Party();
        party2.setId(party1.getId());
        assertThat(party1).isEqualTo(party2);
        party2.setId(2L);
        assertThat(party1).isNotEqualTo(party2);
        party1.setId(null);
        assertThat(party1).isNotEqualTo(party2);
    }
}
