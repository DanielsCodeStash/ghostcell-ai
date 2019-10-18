package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;
import com.ghostcell.container.Owner;
import com.ghostcell.priomodel.FactoryPrio;
import com.ghostcell.priomodel.PrioList;
import com.ghostcell.priomodel.Weight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoostPrioritizationModel {

    private GameState gameState;

    private Map<Integer, FactoryCyborgPrioHistory> factoryIdToHistory = new HashMap<>();

    private double meanDistanceToEnemyFactory;
    private double minDistanceToEnemyFactory;
    private double maxDistanceToEnemyFactory;

    private double meanDistanceToEnemyTroop;

    public BoostPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    private void calculateMeanDistances() {
        List<Factory> enemyFactories = gameState.getFactoryListByOwner(Owner.ENEMY);
        List<Factory> ownFactories = gameState.getFactoryListByOwner(Owner.YOU);

        double totalDistance = 0;
        minDistanceToEnemyFactory = Double.MAX_VALUE;
        maxDistanceToEnemyFactory = 0;

        for(Factory ownFactory : ownFactories) {
            double ownFactoryDistance = 0;

            for(Factory enemyFactory : enemyFactories) {
                double dist = ownFactory.distanceTo(enemyFactory);
                totalDistance += dist;
                ownFactoryDistance += dist;

            }

            minDistanceToEnemyFactory = Math.min(minDistanceToEnemyFactory, ownFactoryDistance);
            maxDistanceToEnemyFactory = Math.max(maxDistanceToEnemyFactory, ownFactoryDistance);
        }

        meanDistanceToEnemyFactory = totalDistance / ownFactories.size();
    }

    private double getTotalDistanceToEnemyFactory(Factory activeFactory) {
        return gameState.getFactoryListByOwner(Owner.ENEMY)
                .stream()
                .mapToDouble(f -> f.distanceTo(activeFactory))
                .sum();
    }

    public PrioList getPrioList() {

        calculateMeanDistances();

        PrioList prioList = new PrioList();

        List<Factory> myFactories = gameState.getFactoryListByOwner(Owner.YOU);
        for(Factory activeFactory : myFactories) {

            FactoryPrio factoryPrio = new FactoryPrio(activeFactory);

            if(activeFactory.getProduction() == 3) {
                continue;
            }

            double factoryTotalDistanceImportance = 0.4;
            double factoryMeanDistanceImportance = 0.4;
            double turnBonusImportance = 0.5;

            factoryPrio.addWeight(new Weight()
                    .setLabel("total_e_dist")
                    .setMaxValue(50)
                    .setValue(getTotalDistanceToEnemyFactory(activeFactory))
                    .setReverse(false)
                    .setImportance(factoryTotalDistanceImportance));

            factoryPrio.addWeight(new Weight()
                    .setLabel("mean_dist")
                    .setMaxValue(5)
                    .setValue(getTotalDistanceToEnemyFactory(activeFactory) / gameState.getFactoryListByOwner(Owner.ENEMY).size())
                    .setReverse(false)
                    .setImportance(factoryMeanDistanceImportance));

            factoryPrio.addWeight(new Weight()
                    .setLabel("turn_bonus")
                    .setMaxValue(50)
                    .setValue(gameState.getTurnNumber() * 2)
                    .setReverse(false)
                    .setImportance(turnBonusImportance));
            

            factoryPrio.setFactoryPrio(factoryPrio.calculatePreliminaryPrio());
            prioList.add(factoryPrio);

        }

        return prioList;
    }



}
