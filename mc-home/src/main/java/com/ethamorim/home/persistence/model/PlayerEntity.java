package com.ethamorim.home.persistence.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidade que representa o jogador.
 * Carrega informações básicas de identidade e preferências de configurações.
 *
 * @author ethamorim
 */
@Entity
public class PlayerEntity {
    public PlayerEntity() {}

    /**
     * Id do usuário.
     * Chave primária não é o UUID por questões de performance
     * e possíveis alterações.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    /**
     * UUID do jogador.
     * Não está anotado como @NaturalId pois nem sempre se
     * tem o UUID do jogador quando comparado ao nickname,
     * e anotar 2 atributos com @NaturalId faz com que
     * Hibernate exija ambos quando usado `session.byNaturalId`
     */
    @Column(unique = true, nullable = false)
    UUID uuid;

    /**
     * Nickname do jogador nas sessões do servidor.
     */
    @NaturalId
    @Column(nullable = false)
    String nickname;

    /**
     * O intervalo em milissegundos no qual o jogador
     * pode utilizar o comando home novamente.
     */
    int cooldown;

    /**
     * Ativa partículas ao se teletransportar caso `true`
     */
    boolean particlesActive;

    /**
     * Determina a última vez que o jogador usou
     * o comando de /home.
     * Usado em conjunto com `cooldown` para determinar
     * se o jogador pode usar o comando novamente.
     */
    Instant lastIssued;

    /*
        Getters e setters
     */
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

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isParticlesActive() {
        return particlesActive;
    }

    public void setParticlesActive(boolean particlesActive) {
        this.particlesActive = particlesActive;
    }

    public Instant getLastIssued() {
        return lastIssued;
    }

    public void setLastIssued(Instant lastIssued) {
        this.lastIssued = lastIssued;
    }
}
