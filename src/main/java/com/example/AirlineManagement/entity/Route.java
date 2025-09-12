package com.example.AirlineManagement.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ROUTES")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Integer routeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_terminal_id", nullable = false)
    private com.example.AirlineManagement.entity.Terminal originTerminal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_terminal_id", nullable = false)
    private com.example.AirlineManagement.entity.Terminal destinationTerminal;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Flight> flights;

    // Constructors
    public Route() {}

    public Route(Airline airline, com.example.AirlineManagement.entity.Terminal originTerminal, com.example.AirlineManagement.entity.Terminal destinationTerminal) {
        this.airline = airline;
        this.originTerminal = originTerminal;
        this.destinationTerminal = destinationTerminal;
    }

    // Getters and Setters
    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public com.example.AirlineManagement.entity.Terminal getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(com.example.AirlineManagement.entity.Terminal originTerminal) {
        this.originTerminal = originTerminal;
    }

    public com.example.AirlineManagement.entity.Terminal getDestinationTerminal() {
        return destinationTerminal;
    }

    public void setDestinationTerminal(com.example.AirlineManagement.entity.Terminal destinationTerminal) {
        this.destinationTerminal = destinationTerminal;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
