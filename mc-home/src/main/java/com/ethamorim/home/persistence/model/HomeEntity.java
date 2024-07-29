package com.ethamorim.home.persistence.model;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

/**
 * Entidade que representa uma "home" salva do jogador.
 * É considerada uma entidade fraca no relacionamento,
 * sendo necessário ter o jogador disponível para fazer as buscas.
 *
 * @author ethamorim
 */
@Entity
public class HomeEntity {
    public HomeEntity() {}

    /**
     * Id incremental da home.
     * Limitações da ORM impedem que `player` seja uma
     * chave composta com `name`, então ao invés disso os
     * dois são anotados como ids naturais.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    /**
     * Nome da home.
     * Utilizado junto com `player` para identificar a home.
     */
    @NaturalId
    String name;

    /**
     * Jogador criador da home.
     * Possui uma relação de um-para-muitos com Jogador,
     * e também é utilizado com `name` para identificar
     * a home.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @NaturalId
    PlayerEntity player;

    /**
     * Coordenadas na qual a home se refere.
     */
    @Column(nullable = false)
    HomeLocation location;


    /*
        Getters e setters
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
