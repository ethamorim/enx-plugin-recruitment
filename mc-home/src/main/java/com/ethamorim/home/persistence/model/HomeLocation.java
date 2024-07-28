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
public record HomeLocation(double x, double y, double z) implements Serializable {}
