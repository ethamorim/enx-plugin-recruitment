package com.ethamorim.home.persistence.model;

import java.io.Serializable;

/**
 * Representa as coordenadas da "home" salva.
 * @param x
 * @param y
 * @param z
 *
 * @author ethamorim
 */
public record HomeLocation(float x, float y, float z) implements Serializable {}
