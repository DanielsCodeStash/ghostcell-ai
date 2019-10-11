package com.ghostcell.priomodel;

public class PrioUtil {

    public static double normalize(double maxValue, double value, boolean reverse) {
        double norm = Math.max(value, 0.00001) / maxValue;
        return reverse ? 1-norm : norm;
    }
}
