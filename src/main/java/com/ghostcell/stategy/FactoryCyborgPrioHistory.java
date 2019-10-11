package com.ghostcell.stategy;

public class FactoryCyborgPrioHistory {
    private int numRounds;
    private double meanProduction;

    private double latestMeanProductionValue;
    private double latestPrioValue;

    public FactoryCyborgPrioHistory(int numRounds, double meanProduction) {
        this.numRounds = numRounds;
        this.meanProduction = meanProduction;
    }

    public int getNumRounds() {
        return numRounds;
    }

    public FactoryCyborgPrioHistory setNumRounds(int numRounds) {
        this.numRounds = numRounds;
        return this;
    }

    public double getMeanProduction() {
        return meanProduction;
    }

    public FactoryCyborgPrioHistory setMeanProduction(double meanProduction) {
        this.meanProduction = meanProduction;
        return this;
    }

    public double getLatestMeanProductionValue() {
        return latestMeanProductionValue;
    }

    public FactoryCyborgPrioHistory setLatestMeanProductionValue(double latestMeanProductionValue) {
        this.latestMeanProductionValue = latestMeanProductionValue;
        return this;
    }

    public double getLatestPrioValue() {
        return latestPrioValue;
    }

    public FactoryCyborgPrioHistory setLatestPrioValue(double latestPrioValue) {
        this.latestPrioValue = latestPrioValue;
        return this;
    }

    @Override
    public String toString() {
        return "FactoryCyborgPrioHistory{" +
                "numRounds=" + numRounds +
                ", meanProduction=" + meanProduction +
                ", latestMeanProductionValue=" + latestMeanProductionValue +
                ", latestPrioValue=" + latestPrioValue +
                '}';
    }
}
