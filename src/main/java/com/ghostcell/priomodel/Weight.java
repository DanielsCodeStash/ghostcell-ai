package com.ghostcell.priomodel;

import com.ghostcell.io.IoUtil;

public class Weight {

    private String label;

    private double minValue = 0;
    private double maxValue;
    private double value;
    private boolean reverse;
    private double importance;

    private double outputWeight;

    private boolean debug = false;

    @Override
    public String toString() {
        String s = IoUtil.round(outputWeight);
        if(debug) {
            s += " (" + value + " on scale " +
                    minValue + " to " +
                    maxValue  + " calc: " +
                    PrioUtil.normalize(maxValue, value, reverse) + " * " +
                    importance + " = " + calculateOutputWeight() + ")";
        }
        return s;
    }

    public double calculateOutputWeight() {
        return PrioUtil.normalize(maxValue, value, reverse) * importance;
    }

    public boolean isDebug() {
        return debug;
    }

    public Weight setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public double getImportance() {
        return importance;
    }

    public double getOutputWeight() {
        return outputWeight;
    }

    public Weight setOutputWeight(double outputWeight) {
        this.outputWeight = outputWeight;
        return this;
    }



    public String getLabel() {
        return label;
    }

    public Weight setLabel(String label) {
        this.label = label;
        return this;
    }

    public double getMinValue() {
        return minValue;
    }

    public Weight setMinValue(double minValue) {
        this.minValue = minValue;
        return this;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public Weight setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public double getValue() {
        return value;
    }

    public Weight setValue(double value) {
        this.value = value;
        return this;
    }

    public boolean isReverse() {
        return reverse;
    }

    public Weight setReverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public double isImportance() {
        return importance;
    }

    public Weight setImportance(double importance) {
        this.importance = importance;
        return this;
    }
}
