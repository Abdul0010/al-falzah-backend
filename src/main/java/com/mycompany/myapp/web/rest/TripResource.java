package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Trip;
import com.mycompany.myapp.repository.TripRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Trip}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripRepository tripRepository;

    public TripResource(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /**
     * {@code POST  /trips} : Create a new trip.
     *
     * @param trip the trip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trip, or with status {@code 400 (Bad Request)} if the trip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trips")
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", trip);
        if (trip.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trip result = tripRepository.save(trip);
        return ResponseEntity.created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trips} : Updates an existing trip.
     *
     * @param trip the trip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trip,
     * or with status {@code 400 (Bad Request)} if the trip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trips")
    public ResponseEntity<Trip> updateTrip(@RequestBody Trip trip) throws URISyntaxException {
        log.debug("REST request to update Trip : {}", trip);
        if (trip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Trip result = tripRepository.save(trip);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trip.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /trips} : get all the trips.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trips in body.
     */
    @GetMapping("/trips")
    public List<Trip> getAllTrips(@RequestParam String from,@RequestParam String to) {
        log.debug("REST request to get all Trips");
        String source =from.isEmpty()?null:from;
        String des =to.isEmpty()?null:to;
        return tripRepository.findtrips(source,des);
    }

    /**
     * {@code GET  /trips/:id} : get the "id" trip.
     *
     * @param id the id of the trip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Optional<Trip> trip = tripRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trip);
    }

    /**
     * {@code DELETE  /trips/:id} : delete the "id" trip.
     *
     * @param id the id of the trip to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        tripRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
