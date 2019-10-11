package com.ghostcell.priomodel;

import com.ghostcell.container.Factory;
import com.ghostcell.io.IoUtil;

import java.util.ArrayList;
import java.util.List;

public class FactoryPrio implements Comparable<FactoryPrio> {

    private Factory originFactory;
    private Factory targetFactory;

    private double factoryPrio;
    private List<Weight> weights = null;

    public FactoryPrio(Factory originFactory) {
        this.originFactory = originFactory;
    }

    public FactoryPrio(Factory originFactory, Factory targetFactory) {
        this.originFactory = originFactory;
        this.targetFactory = targetFactory;
    }

    public FactoryPrio addWeight(Weight w) {
        if(weights == null) {
            weights = new ArrayList<>();
        }

        double outputWeight = w.calculateOutputWeight();
        w.setOutputWeight(outputWeight);

        weights.add(w);

        return this;
    }

    public double calculatePreliminaryPrio() {

        double totalWeight = 0;

        for(Weight w : weights) {
            totalWeight += w.getOutputWeight();
        }

        factoryPrio =  PrioUtil.normalize(weights.size(), totalWeight, false);
        return factoryPrio;
    }


    public Factory getOriginFactory() {
        return originFactory;
    }

    public FactoryPrio setOriginFactory(Factory originFactory) {
        this.originFactory = originFactory;
        return this;
    }

    public Factory getTargetFactory() {
        return targetFactory;
    }

    public FactoryPrio setTargetFactory(Factory targetFactory) {
        this.targetFactory = targetFactory;
        return this;
    }

    public double getFactoryPrio() {
        return factoryPrio;
    }

    public FactoryPrio setFactoryPrio(double factoryPrio) {
        this.factoryPrio = factoryPrio;
        return this;
    }

    public List<Weight> getWeights() {
        return weights;
    }

    public FactoryPrio setWeights(List<Weight> weights) {
        this.weights = weights;
        return this;
    }


    @Override
    public int compareTo(FactoryPrio o) {
        return Double.compare(o.getFactoryPrio(), getFactoryPrio());
    }


    @Override
    public String toString() {

        String s = "";
        if(targetFactory == null) {
            s = factoryIdToStr(originFactory.getId()) + " | " + IoUtil.round(factoryPrio) + " | ";
        } else {

            s = factoryIdToStr(originFactory.getId()) + " -> " + factoryIdToStr(targetFactory.getId()) + " | " + IoUtil.round(factoryPrio) + " | ";
        }

        for(int i = 0; i < weights.size(); i++) {
            Weight w = weights.get(i);
            boolean isLast = i == weights.size()-1;
            s += w.getLabel() + ": " + w.toString();

            if(!isLast) {
                s += " | ";
            }
        }
        return s;
    }



    private String factoryIdToStr(int id) {
        if(id < 10) {
            return id + " ";
        } else {
            return id + "";
        }
    }

    public void print() {
        System.err.println(this.toString());
    }
}
