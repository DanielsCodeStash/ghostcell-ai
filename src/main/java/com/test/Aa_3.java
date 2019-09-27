package com.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Aa_3 extends Ai {

    private boolean firstRound = true;

    @Override
    public void run(GameData gameData) {
        init(gameData);

        if(firstRound) {
            initialBombing(gameData);
            firstRound = false;
        }

        for(Factory activeFactory : ownFactories) {

            System.err.println("Active factory: " + activeFactory.getId());
            List<Factory> prioList = getPrioList(gameData, activeFactory);
            //prioList.forEach(System.err::println);


            for(Factory targetFactory : prioList) {

                if(activeFactory.getId() == targetFactory.getId())
                    continue;

                int availableCyborgs = activeFactory.getCyborgs();

                availableCyborgs += getSumOfArrivingTroops(gameData, activeFactory);


                if(availableCyborgs <= 1)
                    continue;

                if(targetFactory.getOwner() == Owner.YOU) {

                    int sumArriving = getSumOfArrivingTroops(gameData, targetFactory);
                    int balance = sumArriving + targetFactory.getCyborgs();
                    if(balance < 0) {
                        int toSend = Math.min(balance, availableCyborgs);
                        sendOrder(new Order(activeFactory, targetFactory, toSend), gameData);
                    }


                } else {
                    int required = getNumberOfCyborgsToSendToTakeFactory(activeFactory, targetFactory, gameData);
                    if(required < 0)
                        continue;

                    if(required > availableCyborgs && targetFactory.getOwner() == Owner.NEUTRAL)
                        continue;

                    sendOrder(new Order(activeFactory, targetFactory, Math.min(availableCyborgs, required)), gameData);
                }

            }
        }

        System.err.println("done");

    }




    public void initialBombing(GameData gameData) {
        Factory enemyStartingFactory = enemyFactories.get(0);
        Factory ownStartingFactory = ownFactories.get(0);

        int bombsRemaining = 2;



        List<Factory> enemyPrioList = getPrioList(gameData, enemyStartingFactory);

        for(Factory f : enemyPrioList) {
            if(bombsRemaining == 0)
                return;

            if(f.getOwner() == Owner.YOU)
                continue;

            Order bomb = new Order()
                    .setFrom(ownStartingFactory)
                    .setTo(f)
                    .setBomb(true);

            gameData.addOrder(bomb);

            bombsRemaining--;
        }
    }

    private void sendOrder(Order order, GameData gameData) {

        if(!order.isBomb() && order.getNum() < 1)
            return;

        order.getFrom().setCyborgs(order.getFrom().getCyborgs() - order.getNum());

        Troop t = new Troop()
                .setId(-1)
                .setOwner(Owner.YOU)
                .setLeavingFactory(order.getFrom().getId())
                .setTargetFactory(order.getTo().getId())
                .setNumCyborgs(order.getNum())
                .setTurnsUntilArrival(gameData.distanceBetweenFactories(order.getFrom(), order.getTo(), gameData));

        gameData.addOutgoingTroop(t);

        gameData.addOrder(order);
    }

    private int getNumberOfCyborgsToSendToTakeFactory(Factory fromFactory, Factory toFactory, GameData gameData) {

        int distance = gameData.distanceBetweenFactories(fromFactory, toFactory, gameData);

        int sumFromArriving = getSumOfArrivingTroops(gameData, toFactory);

        if(toFactory.getOwner() == Owner.NEUTRAL) {
            return toFactory.getCyborgs() + sumFromArriving + 1;
        } else if(toFactory.getOwner() == Owner.ENEMY) {
            return toFactory.getCyborgs() + toFactory.getProduction() * distance + sumFromArriving + 1;
        }

        System.err.println("FAIL " + fromFactory + ", " + toFactory);
        System.exit(0);
        return -1;
    }


    private int getSumOfArrivingTroops(GameData gameData, Factory factory) {
        return getSumOfArrivingTroops(gameData, factory, false);
    }

    private int getSumOfArrivingTroops(GameData gameData, Factory factory, boolean debug) {


        List<Troop> troopsHeadingToPlanet = gameData.getTroops().stream().filter(to -> to.getTargetFactory() == factory.getId()).collect(Collectors.toList());
        List<Troop> ownTroopsHeadingOut = gameData.getOutGoingTroops().stream().filter(to -> to.getTargetFactory() == factory.getId()).collect(Collectors.toList());
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
            System.err.println("sumFromEnemy: " + sumFromEnemy + ", sumFromMe: " + sumFromYou + " totalTroops: " + gameData.getTroops().size() + " heading towards me: " + troopsHeadingToPlanet.size() + ", balance =" + balance);

        return balance;
    }


    private List<Factory> getPrioList(GameData gameData, Factory activeFactory) {

        for(Factory f : gameData.getFactories()) {
            if(f.getId() == activeFactory.getId())
                continue;

            double distance = gameData.distanceBetweenFactories(activeFactory, f, gameData);

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

        List<Factory> prioFactories = new ArrayList<>(gameData.getFactories());
        prioFactories.sort(compareByPrio);


        return prioFactories;
    }

    private double normalize(double valueMin, double valueMax, double value, boolean reverse) {
        double norm =   Math.max(value, 0.00001) / valueMax;
        return reverse ? 1-norm : norm;
    }

}
