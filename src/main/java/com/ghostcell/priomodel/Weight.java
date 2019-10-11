package com.ghostcell.priomodel;

public class Weight {
    private String label;
    private double weight;

    public Weight(String label, double weight) {
        this.label = label;
        this.weight = weight;
    }

    public String getLabel() {
        return label;
    }

    public double getWeight() {
        return weight;
    }
}
