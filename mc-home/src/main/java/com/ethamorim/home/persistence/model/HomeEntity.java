package com.ethamorim.home.persistence.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

@Entity
public class HomeEntity {
    public HomeEntity() {}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NaturalId
    @Column(unique = false)
    String name;

    @Column(nullable = false)
    HomeLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @NaturalId
    PlayerEntity player;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HomeLocation getLocation() {
        return location;
    }

    public void setLocation(HomeLocation location) {
        this.location = location;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
