package com.ghostcell;

import com.ghostcell.container.Factory;
import com.ghostcell.container.Order;
import com.ghostcell.container.Owner;
import com.ghostcell.container.Troop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PrioritizationStrategy extends Strategy {


    public PrioritizationStrategy(GameState gameState) {
        super(gameState);
    }

    @Override
    public List<Order> run(GameState gameData) {
        initRound();

        if(gameData.getTurnNumber() == 0) {
            initialBombing();
        }

        for(Factory activeFactory : ownFactories) {

            for(Factory targetFactory : getPrioList(activeFactory)) {
                evaluateAction(activeFactory, targetFactory);
            }
        }

        return orders;
    }

    private void evaluateAction(Factory activeFactory, Factory targetFactory) {

        if(activeFactory.getId() == targetFactory.getId())
            return;

        int availableCyborgs = activeFactory.getCyborgs();

        availableCyborgs += getSumOfArrivingTroops(activeFactory);

        if(availableCyborgs <= 1)
            return;

        if(targetFactory.ownerIsMe()) {

            int sumArriving = getSumOfArrivingTroops(targetFactory);
            int balance = sumArriving + targetFactory.getCyborgs();
            if(balance < 0) {
                int toSend = Math.min(balance, availableCyborgs);
                sendOrder(new Order(activeFactory, targetFactory, toSend));
            }

        } else {

            int required = getNumberOfCyborgsToSendToTakeFactory(activeFactory, targetFactory);
            if(required < 0)
                return;

            if(required > availableCyborgs && targetFactory.ownerIsNone())
                return;

            sendOrder(new Order(activeFactory, targetFactory, Math.min(availableCyborgs, required)));
        }
    }



    private void initialBombing() {
        Factory enemyStartingFactory = enemyFactories.get(0);
        Factory ownStartingFactory = ownFactories.get(0);

        int bombsRemaining = 2;

        List<Factory> enemyPrioList = getPrioList(enemyStartingFactory);

        for(Factory f : enemyPrioList) {
            if(bombsRemaining == 0)
                return;

            if(f.getOwner() == Owner.YOU)
                continue;

            Order bomb = new Order()
                    .setFrom(ownStartingFactory)
                    .setTo(f)
                    .setBomb(true);

            orders.add(bomb);

            bombsRemaining--;
        }
    }

    private void sendOrder(Order order) {

        if(!order.isBomb() && order.getNum() < 1)
            return;

        order.getFrom().setCyborgs(order.getFrom().getCyborgs() - order.getNum());

        Troop t = new Troop()
                .setId(-1)
                .setOwner(Owner.YOU)
                .setLeavingFactory(order.getFrom().getId())
                .setTargetFactory(order.getTo().getId())
                .setNumCyborgs(order.getNum())
                .setTurnsUntilArrival(order.getFrom().distanceTo(order.getTo()));


        gameState.addOutgoingTroop(t);

        orders.add(order);
    }

    private int getNumberOfCyborgsToSendToTakeFactory(Factory fromFactory, Factory toFactory) {

        int distance = fromFactory.distanceTo(toFactory);

        int sumFromArriving = getSumOfArrivingTroops(toFactory);

        if(toFactory.ownerIsNone()) {
            return toFactory.getCyborgs() + sumFromArriving + 1;
        } else if(toFactory.ownerIsEnemy()) {
            return toFactory.getCyborgs() + toFactory.getProduction() * distance + sumFromArriving + 1;
        }

        System.err.println("FAIL " + fromFactory + ", " + toFactory);
        System.exit(0);
        return -1;
    }


    private int getSumOfArrivingTroops(Factory factory) {
        return getSumOfArrivingTroops(factory, false);
    }

    private int getSumOfArrivingTroops(Factory factory, boolean debug) {

        List<Troop> troopsHeadingToPlanet = gameState.getTroops().stream().filter(to -> to.getTargetFactory() == factory.getId()).collect(Collectors.toList());
        List<Troop> ownTroopsHeadingOut = gameState.getOutGoingTroops().stream().filter(to -> to.getTargetFactory() == factory.getId()).collect(Collectors.toList());
        troopsHeadingToPlanet.addAll(ownTroopsHeadingOut);

        int balance = 0;
        int sumFromEnemy = 0;
        int sumFromYou = 0;
        for(Troop t : troopsHeadingToPlanet) {
            if(t.getOwner() == Owner.YOU) {
                balance += t.getNumCyborgs();
                sumFromYou += t.getNumCyborgs();
            } else {
                balance -= t.getNumCyborgs();
                sumFromEnemy += t.getNumCyborgs();
            }
        }

        if(debug)
            System.err.println("sumFromEnemy: " + sumFromEnemy + ", sumFromMe: " + sumFromYou + " totalTroops: " + gameState.getTroops().size() + " heading towards me: " + troopsHeadingToPlanet.size() + ", balance =" + balance);

        return balance;
    }


    private List<Factory> getPrioList(Factory activeFactory) {

        for(Factory f : gameState.getFactories()) {
            if(f.getId() == activeFactory.getId())
                continue;

            double distance = activeFactory.distanceTo(f);

            int production = f.getProduction();

            double distanceImportance = 0.9; // 0 - 1
            double productionImportance = 0.2; // 0 - 1


            double distanceWeight = normalize(0, 20, distance, true) * distanceImportance;
            double productionWeight = normalize(0, 3, production, false) * productionImportance;

//            System.err.println(f.getId() + " - d: " + distanceWeight + " (" + distance + "), "
//                    + ", p: " + productionWeight + " (" + production + ")");

            double prio = normalize(0, 2, distanceWeight + productionWeight, false);

            if(production == 0)
                prio = 0.001;

            f.setPrioWeight(prio);
        }

        Comparator<Factory> compareByPrio = (f, f2) -> {
            Double fp1 = f.getPrioWeight();
            Double fp2 = f2.getPrioWeight();
            return fp2.compareTo(fp1);
        };

        List<Factory> prioFactories = new ArrayList<>(gameState.getFactories());
        prioFactories.sort(compareByPrio);


        return prioFactories;
    }

    private double normalize(double valueMin, double valueMax, double value, boolean reverse) {
        double norm =   Math.max(value, 0.00001) / valueMax;
        return reverse ? 1-norm : norm;
    }

}
