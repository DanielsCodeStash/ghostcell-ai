package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;
import com.ghostcell.priomodel.FactoryPrio;
import com.ghostcell.priomodel.PrioList;
import com.ghostcell.priomodel.Weight;

public class BombPrioritizationModel {

    private GameState gameState;

    private static final boolean debug = true;

    public BombPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public PrioList getPrioList(Factory originFactory) {

        PrioList prioList = new PrioList();

        for(Factory target : gameState.getFactories()) {

            FactoryPrio factoryPrio = new FactoryPrio(originFactory, target);

            if(target.getId() == originFactory.getId() || !target.ownerIsEnemy()) {
                continue;
            }

            double distanceImportance = 0.2;
            double targetProductionImportance = 0.9;
            double targetCyborgsImportance = 0.5;

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
                    .setImportance(targetProductionImportance));

            factoryPrio.addWeight(new Weight() // target cyborgs
                    .setLabel("t_cybo")
                    .setMaxValue(100)
                    .setValue(target.getNumCyborgs())
                    .setReverse(true)
                    .setImportance(targetCyborgsImportance));


           double prio = factoryPrio.calculatePreliminaryPrio();

            // round number bonus
            prio = Math.min(1, prio + gameState.getTurnNumber() * 0.01);

            // avoid bombing the same thing twice
            boolean bombAlreadyHeadingToPlanet = gameState.getBombs().stream()
                    .anyMatch(b -> b.getTargetFactory() == target.getId());

            if(bombAlreadyHeadingToPlanet) {
                prio = 0.001;
            }


            factoryPrio.setFactoryPrio(prio);

            prioList.add(factoryPrio);
        }


        return prioList.sort();
    }
}
