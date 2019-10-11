package com.ghostcell.priomodel;

import com.ghostcell.container.Factory;

import java.util.List;

public class FactoryPrio implements Comparable<FactoryPrio> {

    private Factory originFactory;
    private Factory targetFactory;

    private double factoryPrio;
    private List<Weight> weights;


    public FactoryPrio(Factory originFactory, Factory targetFactory) {
        this.originFactory = originFactory;
        this.targetFactory = targetFactory;
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
}
