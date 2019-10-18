package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;
import com.ghostcell.priomodel.FactoryPrio;
import com.ghostcell.priomodel.PrioList;
import com.ghostcell.priomodel.Weight;

import java.util.Optional;

public class CyborgPrioritizationModel {

    private GameState gameState;

    private PrioList boostPrioList;

    public CyborgPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public void setBoostPrioList(PrioList boostPrioList) {
        this.boostPrioList = boostPrioList;
    }

    public PrioList getPrioList(Factory originFactory) {

        PrioList prioList = new PrioList();

        for(Factory target : gameState.getFactories()) {

            FactoryPrio factoryPrio = new FactoryPrio(originFactory, target);

            if(target.getId() == originFactory.getId())
                continue;

            double distanceImportance = 0.9;
            double productionImportance = 0.2;
            double boostPrioImportance = 0.8;

            factoryPrio.addWeight(new Weight()
                    .setLabel("dist")
                    .setMaxValue(20)
                    .setValue(originFactory.distanceTo(target))
                    .setReverse(true)
                    .setImportance(distanceImportance));

            factoryPrio.addWeight(new Weight() // target production
                    .setLabel("t_prod")
                    .setMaxValue(3)
                    .setValue(target.getProduction())
                    .setReverse(false)
                    .setImportance(productionImportance));

            factoryPrio.addWeight(new Weight() // target production
                    .setLabel("boost")
                    .setMaxValue(1)
                    .setValue(calculateBoostValue(target))
                    .setReverse(false)
                    .setImportance(boostPrioImportance));

            double prio = factoryPrio.calculatePreliminaryPrio();

            if(prio == 0) {
                prio = 0.001;
            }

            factoryPrio.setFactoryPrio(prio);
            prioList.add(factoryPrio);
        }

        return prioList.sort();
    }

    private double calculateBoostValue(Factory target) {
        double boostValue = 0;
        if(target.ownerIsMe()) {
            Optional<FactoryPrio> boostPrio = boostPrioList.get().stream()
                    .filter(p -> p.getOriginFactory().getId() == target.getId())
                    .findFirst();

            if(boostPrio.isPresent()) {
                boostValue = boostPrio.get().getFactoryPrio();
            }
        }
        return boostValue;
    }
}
