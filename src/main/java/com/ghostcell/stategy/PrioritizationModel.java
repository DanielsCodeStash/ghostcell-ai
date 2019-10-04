package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.container.Factory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrioritizationModel {

    private GameState gameState;

    public PrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public List<Factory> getPrioList(Factory activeFactory) {

        for(Factory f : gameState.getFactories()) {

            if(f.getId() == activeFactory.getId())
                continue;

            double distance = activeFactory.distanceTo(f);

            int production = f.getProduction();

            double distanceImportance = 0.9; // 0 - 1
            double productionImportance = 0.2; // 0 - 1


            double distanceWeight = normalize( 20, distance, true) * distanceImportance;
            double productionWeight = normalize( 3, production, false) * productionImportance;

//          System.err.println(f.getId() + " - d: " + distanceWeight + " (" + distance + "), " + ", p: " + productionWeight + " (" + production + ")");

            double prio = normalize(2, distanceWeight + productionWeight, false);

            if(production == 0) {
                prio = 0.001;
            }

            f.setFactoryPrio(prio);
        }

        Comparator<Factory> compareByPrio = (f, f2) -> {
            Double fp1 = f.getFactoryPrio();
            Double fp2 = f2.getFactoryPrio();
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
