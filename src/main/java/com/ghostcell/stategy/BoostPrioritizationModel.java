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

    public BoostPrioritizationModel(GameState gameState) {
        this.gameState = gameState;
    }

    public void registerTopCyborgPrioForFactory(FactoryPrio prio) {
        if(!prio.getOriginFactory().ownerIsMe()) {
            return;
        }

        FactoryCyborgPrioHistory history = factoryIdToHistory.get(prio.getOriginFactory().getId());
        if(history == null) {

            history = new FactoryCyborgPrioHistory(1, prio.getFactoryPrio());
            factoryIdToHistory.put(prio.getOriginFactory().getId(), history);

        }
        else {

            double oldMean = history.getMeanProduction();
            double newValue = prio.getFactoryPrio();
            double numValues = history.getNumRounds();

            history
                    .setLatestMeanProductionValue(oldMean)
                    .setLatestPrioValue(newValue)
                    .setMeanProduction(oldMean + (newValue-oldMean) / numValues)
                    .setNumRounds(history.getNumRounds()+1);
        }
    }

    public PrioList getPrioList() {

        PrioList prioList = new PrioList();

        List<Factory> myFactories = gameState.getFactoryListByOwner(Owner.YOU);
        for(Factory activeFactory : myFactories) {

            FactoryPrio factoryPrio = new FactoryPrio(activeFactory);

            FactoryCyborgPrioHistory history = factoryIdToHistory.get(activeFactory.getId());
            if(history == null) {
                continue;
            }

            if(history.getNumRounds() < 5 || activeFactory.getProduction() == 3) {
                continue;
            }

            double percentBelowMean;

            double mean = history.getLatestMeanProductionValue();
            double latestPrio = history.getLatestPrioValue();

            if(latestPrio > mean) {
                percentBelowMean = 0;
            } else {
                percentBelowMean = mean / latestPrio;
                if(percentBelowMean > 100) {
                    percentBelowMean = 100;
                }
            }

            double belowMeanImportance = 1.0;

            factoryPrio.addWeight(new Weight()
                    .setLabel("below_mean")
                    .setMaxValue(100)
                    .setValue(percentBelowMean)
                    .setReverse(true)
                    .setImportance(belowMeanImportance));
            

            factoryPrio.setFactoryPrio(factoryPrio.calculatePreliminaryPrio());
            prioList.add(factoryPrio);

        }

        return prioList;
    }


}
