package com.ghostcell.priomodel;

import com.ghostcell.container.Factory;

import java.util.ArrayList;
import java.util.List;

public class WeightDebugger {

    private boolean debug;
    private Factory fromFactory;
    private Factory toFactory;
    private double totalPrio;
    private List<Weight> weights = new ArrayList<>();

    public WeightDebugger(boolean debug, Factory fromFactory, Factory toFactory, double totalPrio) {
        this.debug = debug;
        this.fromFactory = fromFactory;
        this.toFactory = toFactory;
        this.totalPrio = totalPrio;
    }


    public WeightDebugger addWeight(String label, double weight) {
        weights.add(new Weight(label, weight));
        return this;
    }

    public WeightDebugger print() {
        if(debug) {
            String s = fromFactory.getId() + " -> " + toFactory.getId() + " | " + totalPrio + " | ";
            for (Weight w : weights) {
                s += w.getLabel() + ": " + w.getWeight() + " ";
            }
            System.err.println(s);
        }
        return this;
    }
}
