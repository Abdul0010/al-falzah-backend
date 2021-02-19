package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AlfalzahApp;
import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.repository.TripRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@SpringBootTest(classes = AlfalzahApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TripResourceIT {

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CAR = "AAAAAAAAAA";
    private static final String UPDATED_CAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_PASSENGERS = 1;
    private static final Integer UPDATED_PASSENGERS = 2;

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip()
            .source(DEFAULT_SOURCE)
            .destination(DEFAULT_DESTINATION)
            .startTime(DEFAULT_START_TIME)
            .car(DEFAULT_CAR)
            .passengers(DEFAULT_PASSENGERS)
            .contactNo(DEFAULT_CONTACT_NO);
        return trip;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip()
            .source(UPDATED_SOURCE)
            .destination(UPDATED_DESTINATION)
            .startTime(UPDATED_START_TIME)
            .car(UPDATED_CAR)
            .passengers(UPDATED_PASSENGERS)
            .contactNo(UPDATED_CONTACT_NO);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();
        // Create the Trip
        restTripMockMvc.perform(post("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTrip.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTrip.getCar()).isEqualTo(DEFAULT_CAR);
        assertThat(testTrip.getPassengers()).isEqualTo(DEFAULT_PASSENGERS);
        assertThat(testTrip.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
    }

    @Test
    @Transactional
    public void createTripWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // Create the Trip with an existing ID
        trip.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc.perform(post("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc.perform(get("/api/trips?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].car").value(hasItem(DEFAULT_CAR)))
            .andExpect(jsonPath("$.[*].passengers").value(hasItem(DEFAULT_PASSENGERS)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO)));
    }
    
    @Test
    @Transactional
    public void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.car").value(DEFAULT_CAR))
            .andExpect(jsonPath("$.passengers").value(DEFAULT_PASSENGERS))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO));
    }
    @Test
    @Transactional
    public void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get("/api/trips/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).get();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .source(UPDATED_SOURCE)
            .destination(UPDATED_DESTINATION)
            .startTime(UPDATED_START_TIME)
            .car(UPDATED_CAR)
            .passengers(UPDATED_PASSENGERS)
            .contactNo(UPDATED_CONTACT_NO);

        restTripMockMvc.perform(put("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrip)))
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTrip.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testTrip.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTrip.getCar()).isEqualTo(UPDATED_CAR);
        assertThat(testTrip.getPassengers()).isEqualTo(UPDATED_PASSENGERS);
        assertThat(testTrip.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc.perform(put("/api/trips")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Delete the trip
        restTripMockMvc.perform(delete("/api/trips/{id}", trip.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
