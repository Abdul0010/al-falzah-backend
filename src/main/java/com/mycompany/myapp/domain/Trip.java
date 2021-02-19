package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "car")
    private String car;

    @Column(name = "passengers")
    private Integer passengers;

    @Column(name = "contact_no")
    private String contactNo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public Trip source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public Trip destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Trip startTime(Instant startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getCar() {
        return car;
    }

    public Trip car(String car) {
        this.car = car;
        return this;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public Trip passengers(Integer passengers) {
        this.passengers = passengers;
        return this;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public String getContactNo() {
        return contactNo;
    }

    public Trip contactNo(String contactNo) {
        this.contactNo = contactNo;
        return this;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trip)) {
            return false;
        }
        return id != null && id.equals(((Trip) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", car='" + getCar() + "'" +
            ", passengers=" + getPassengers() +
            ", contactNo='" + getContactNo() + "'" +
            "}";
    }
}
