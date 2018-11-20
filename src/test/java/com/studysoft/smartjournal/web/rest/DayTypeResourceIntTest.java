package com.studysoft.smartjournal.web.rest;

import com.studysoft.smartjournal.SmartjournalApp;

import com.studysoft.smartjournal.domain.DayType;
import com.studysoft.smartjournal.repository.DayTypeRepository;
import com.studysoft.smartjournal.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.List;


import static com.studysoft.smartjournal.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.studysoft.smartjournal.domain.enumeration.Type;
/**
 * Test class for the DayTypeResource REST controller.
 *
 * @see DayTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartjournalApp.class)
public class DayTypeResourceIntTest {

    private static final Type DEFAULT_TYPE = Type.SIMPLE;
    private static final Type UPDATED_TYPE = Type.LAB;

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DayTypeRepository dayTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDayTypeMockMvc;

    private DayType dayType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DayTypeResource dayTypeResource = new DayTypeResource(dayTypeRepository);
        this.restDayTypeMockMvc = MockMvcBuilders.standaloneSetup(dayTypeResource)
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
    public static DayType createEntity(EntityManager em) {
        DayType dayType = new DayType()
            .type(DEFAULT_TYPE)
            .score(DEFAULT_SCORE)
            .description(DEFAULT_DESCRIPTION)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return dayType;
    }

    @Before
    public void initTest() {
        dayType = createEntity(em);
    }

    @Test
    @Transactional
    public void createDayType() throws Exception {
        int databaseSizeBeforeCreate = dayTypeRepository.findAll().size();

        // Create the DayType
        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isCreated());

        // Validate the DayType in the database
        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DayType testDayType = dayTypeList.get(dayTypeList.size() - 1);
        assertThat(testDayType.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDayType.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testDayType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDayType.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDayType.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createDayTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dayTypeRepository.findAll().size();

        // Create the DayType with an existing ID
        dayType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        // Validate the DayType in the database
        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayTypeRepository.findAll().size();
        // set the field null
        dayType.setType(null);

        // Create the DayType, which fails.

        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayTypeRepository.findAll().size();
        // set the field null
        dayType.setScore(null);

        // Create the DayType, which fails.

        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayTypeRepository.findAll().size();
        // set the field null
        dayType.setCreated(null);

        // Create the DayType, which fails.

        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = dayTypeRepository.findAll().size();
        // set the field null
        dayType.setUpdated(null);

        // Create the DayType, which fails.

        restDayTypeMockMvc.perform(post("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDayTypes() throws Exception {
        // Initialize the database
        dayTypeRepository.saveAndFlush(dayType);

        // Get all the dayTypeList
        restDayTypeMockMvc.perform(get("/api/day-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getDayType() throws Exception {
        // Initialize the database
        dayTypeRepository.saveAndFlush(dayType);

        // Get the dayType
        restDayTypeMockMvc.perform(get("/api/day-types/{id}", dayType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dayType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDayType() throws Exception {
        // Get the dayType
        restDayTypeMockMvc.perform(get("/api/day-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDayType() throws Exception {
        // Initialize the database
        dayTypeRepository.saveAndFlush(dayType);

        int databaseSizeBeforeUpdate = dayTypeRepository.findAll().size();

        // Update the dayType
        DayType updatedDayType = dayTypeRepository.findById(dayType.getId()).get();
        // Disconnect from session so that the updates on updatedDayType are not directly saved in db
        em.detach(updatedDayType);
        updatedDayType
            .type(UPDATED_TYPE)
            .score(UPDATED_SCORE)
            .description(UPDATED_DESCRIPTION)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restDayTypeMockMvc.perform(put("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDayType)))
            .andExpect(status().isOk());

        // Validate the DayType in the database
        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeUpdate);
        DayType testDayType = dayTypeList.get(dayTypeList.size() - 1);
        assertThat(testDayType.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDayType.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testDayType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDayType.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDayType.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingDayType() throws Exception {
        int databaseSizeBeforeUpdate = dayTypeRepository.findAll().size();

        // Create the DayType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDayTypeMockMvc.perform(put("/api/day-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dayType)))
            .andExpect(status().isBadRequest());

        // Validate the DayType in the database
        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDayType() throws Exception {
        // Initialize the database
        dayTypeRepository.saveAndFlush(dayType);

        int databaseSizeBeforeDelete = dayTypeRepository.findAll().size();

        // Get the dayType
        restDayTypeMockMvc.perform(delete("/api/day-types/{id}", dayType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DayType> dayTypeList = dayTypeRepository.findAll();
        assertThat(dayTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DayType.class);
        DayType dayType1 = new DayType();
        dayType1.setId(1L);
        DayType dayType2 = new DayType();
        dayType2.setId(dayType1.getId());
        assertThat(dayType1).isEqualTo(dayType2);
        dayType2.setId(2L);
        assertThat(dayType1).isNotEqualTo(dayType2);
        dayType1.setId(null);
        assertThat(dayType1).isNotEqualTo(dayType2);
    }
}
