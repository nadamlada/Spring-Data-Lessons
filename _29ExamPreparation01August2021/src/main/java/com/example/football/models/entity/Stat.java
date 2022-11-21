package com.example.football.models.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stats")
public class Stat extends BaseEntity {
    @Column(nullable = false)
    private Float shooting;
    @Column(nullable = false)
    private Float passing;
    @Column(nullable = false)
    private Float endurance;
    @OneToMany(mappedBy = "stat")
    private List<Player> player;

    public Stat() {

    }

    public List<Player> getPlayer() {
        return player;
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }

    public Float getShooting() {
        return shooting;
    }

    public void setShooting(Float shooting) {
        this.shooting = shooting;
    }

    public Float getPassing() {
        return passing;
    }

    public void setPassing(Float passing) {
        this.passing = passing;
    }

    public Float getEndurance() {
        return endurance;
    }

    public void setEndurance(Float endurance) {
        this.endurance = endurance;
    }
}
