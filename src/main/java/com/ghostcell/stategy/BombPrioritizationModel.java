package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BombPrioritizationModel {

    private GameState gameState;

    private static final boolean debug = true;

    public BombPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Factory> getPrioList(Factory originFactory) {


        for(Factory target : gameState.getFactories()) {

            if(target.getId() == originFactory.getId())
                continue;

            if(!target.ownerIsEnemy()) {
                target.setBombPrio(0);
                continue;
            }

            double distance = originFactory.distanceTo(target);


            double distanceImportance = 0.2;
            double productionImportance = 0.9;
            double ownProductionImportance = 0.5;
            double enemyCyborgsImportance = 0.5;

            // higher is better, so 0 worst and 1 best
            double distanceWeight = normalize( 20, distance, true) * distanceImportance;
            double productionWeight = normalize( 3, target.getProduction(), false) * productionImportance;
            double ownProductionWeight = normalize(3, originFactory.getProduction(), true) * ownProductionImportance;
            double enemyCyborgWeight = normalize(100, originFactory.getProduction(), false) * enemyCyborgsImportance;

            double prio = normalize(4, distanceWeight + productionWeight + ownProductionWeight + enemyCyborgWeight, false);

            // round number bonus
            prio = Math.min(1, prio + gameState.getTurnNumber() * 0.01);

            WeightDebugger debugger = new WeightDebugger(debug, originFactory, target, prio)
                    .addWeight("dist", distanceWeight)
                    .addWeight("production", productionWeight)
                    .addWeight("ownProduction", ownProductionWeight)
                    .addWeight("enemyCyborg", enemyCyborgWeight)
                    .print();

            boolean bombAlreadyHeadingToPlanet = gameState.getBombs().stream()
                    .anyMatch(b -> b.getTargetFactory() == target.getId());

            if(bombAlreadyHeadingToPlanet) {
                prio = 0.0001;
            }

            target.setBombPrio(prio);
        }

        Comparator<Factory> compareByPrio = (f, f2) -> {
            Double fp1 = f.getBombPrio();
            Double fp2 = f2.getBombPrio();
            return fp2.compareTo(fp1);
        };

        List<Factory> prioFactories = new ArrayList<>(gameState.getFactories());
        prioFactories.sort(compareByPrio);

        return prioFactories;
    }


    private double normalize(double valueMax, double value, boolean reverse) {
        double norm = Math.max(value, 0.00001) / valueMax;
        return reverse ? 1-norm : norm;
    }
}