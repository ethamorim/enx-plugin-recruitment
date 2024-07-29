package com.ethamorim.betterwindcharge.key;

/**
 * Enum para centralização dos fatores de modificação da velocidade.
 *
 * @author ethamorim
 */
public enum VelocityWindCharge {
    STATIC("static", 0.0),
    SLOW("slow", 0.05),
    DEFAULT("default", 1.0),
    FAST("fast", 2.0),
    LIGHTNING("lightning", 8.0);

    private final String description;
    private final double value;

    VelocityWindCharge(String description, double value) {
        this.description = description;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }
}
