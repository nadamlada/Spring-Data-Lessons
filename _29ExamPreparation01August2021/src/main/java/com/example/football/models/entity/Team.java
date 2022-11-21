package com.example.football.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @Column(name = "stadium_name", nullable = false)
    private String stadiumName;
    @Column(nullable = false)
    private Integer fanBase;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String history;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Town town;

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public Integer getFanBase() {
        return fanBase;
    }

    public void setFanBase(Integer fanBase) {
        this.fanBase = fanBase;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
