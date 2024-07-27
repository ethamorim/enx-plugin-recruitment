package com.ethamorim.home.persistence.model;

import java.io.Serializable;

public record HomeLocation(float x, float y, float z) implements Serializable {}
