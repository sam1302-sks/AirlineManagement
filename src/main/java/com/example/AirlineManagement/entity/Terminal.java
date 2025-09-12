package com.example.AirlineManagement.entity;

import com.example.AirlineManagement.entity.Route;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TERMINALS")
public class Terminal {
    @Id
    @Column(name = "terminal_id")
    private Integer terminalId;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "originTerminal", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Route> originRoutes;

    @OneToMany(mappedBy = "destinationTerminal", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Route> destinationRoutes;

    // Constructors
    public Terminal() {}

    public Terminal(Integer terminalId, String name, String city, String country) {
        this.terminalId = terminalId;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    // Getters and Setters
    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Route> getOriginRoutes() {
        return originRoutes;
    }

    public void setOriginRoutes(List<Route> originRoutes) {
        this.originRoutes = originRoutes;
    }

    public List<Route> getDestinationRoutes() {
        return destinationRoutes;
    }

    public void setDestinationRoutes(List<Route> destinationRoutes) {
        this.destinationRoutes = destinationRoutes;
    }
}
