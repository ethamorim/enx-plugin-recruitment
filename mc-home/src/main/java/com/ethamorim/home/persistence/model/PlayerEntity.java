package com.ethamorim.home.persistence.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

@Entity
public class PlayerEntity {
    public PlayerEntity() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true, nullable = false)
    UUID uuid;

    @NaturalId
    @Column(nullable = false)
    String nickname;

    short cooldown;

    boolean particlesActive;

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public short getCooldown() {
        return cooldown;
    }

    public void setCooldown(short cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isParticlesActive() {
        return particlesActive;
    }

    public void setParticlesActive(boolean particlesActive) {
        this.particlesActive = particlesActive;
    }
}
