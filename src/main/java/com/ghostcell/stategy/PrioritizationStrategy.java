package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.Strategy;
import com.ghostcell.container.*;
import com.ghostcell.priomodel.FactoryPrio;
import com.ghostcell.priomodel.PrioList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrioritizationStrategy extends Strategy {

    private CyborgPrioritizationModel cyborgPrioModel;

    private BombPrioritizationModel bombPrioModel;

    private BoostPrioritizationModel boostModel;

    private Set<Factory> frozenFactories;

    public PrioritizationStrategy(GameState gameState) {
        super(gameState);
        cyborgPrioModel = new CyborgPrioritizationModel(gameState);
        bombPrioModel = new BombPrioritizationModel(gameState);
        boostModel = new BoostPrioritizationModel(gameState);
    }

    @Override
    public List<Order> run(GameState gameData) {
        initRound();

        frozenFactories = new HashSet<>();

        PrioList boostPrio = boostModel.getPrioList();
        boostPrio.print();

        for(Factory activeFactory : myFactories) {

            for(FactoryPrio prio : boostPrio.get()) {
                evaluateBoostAction(prio);
            }

            PrioList cyborgPrio = cyborgPrioModel.getPrioList(activeFactory);
            cyborgPrioModel.setBoostPrioList(boostPrio);
            //cyborgPrio.print();
            for(FactoryPrio prio : cyborgPrio.get()) {
                if(frozenFactories.contains(prio.getOriginFactory())) {
                    continue;
                }
                evaluateCyborgAction(prio);
            }

            PrioList bombPrio = bombPrioModel.getPrioList(activeFactory);
            for(FactoryPrio prio : bombPrio.get()) {
                evaluateBombAction(prio);
            }


        }

        return orders;
    }

    private void evaluateBoostAction(FactoryPrio prio) {

        if(prio.getFactoryPrio() < 0.5) {
            return;
        }

        if(prio.getOriginFactory().getNumCyborgs() + getSumOfArrivingTroops(prio.getOriginFactory()) < 1) {
            return;
        }

        // clear send orders
        if(prio.getOriginFactory().getNumCyborgs() < 10) {
            frozenFactories.add(prio.getOriginFactory());
            //System.err.println("holding units for boost at factory " + prio.getOriginFactory().getId() + "(currently + " + prio.getOriginFactory().getNumCyborgs() + " ) ");
        }
        else {
            System.err.println("BOOOOOOOOOOST");
            orders.add(
            new Order()
                    .setFrom(prio.getOriginFactory())
                    .setBoost(true));
        }
    }


    private void evaluateBombAction(FactoryPrio prio) {
        Factory originFactory = prio.getOriginFactory();
        Factory targetFactory = prio.getTargetFactory();

        if(gameState.getNumBombsRemaining() == 0)
            return;

        if(originFactory.ownerIsMe() && targetFactory.ownerIsEnemy()) {
            if(prio.getFactoryPrio() > 0.35) {
                Order bomb = new Order()
                        .setFrom(originFactory)
                        .setTo(targetFactory)
                        .setBomb(true);

                orders.add(bomb);
                gameState.removeOneAvailableBomb();
            }
        }
    }

    private void evaluateCyborgAction(FactoryPrio prio) {

        Factory originFactory = prio.getOriginFactory();
        Factory targetFactory = prio.getTargetFactory();

        if(originFactory.getId() == targetFactory.getId())
            return;

        if(orders.stream().anyMatch(o -> o.isBoost() && o.getFrom().getId() == prio.getOriginFactory().getId())) {
            return;
        }

        int availableCyborgs = originFactory.getNumCyborgs() + getSumOfArrivingTroops(originFactory);
        if(availableCyborgs <= 1)
            return;

        if(targetFactory.ownerIsMe()) {

            int balance = targetFactory.getNumCyborgs() + getSumOfArrivingTroops(targetFactory);
            if(balance < 0) {
                int toSend = Math.min(balance, availableCyborgs);
                sendOrder(new Order(originFactory, targetFactory, toSend));
            }

        } else {

            int required = getNumberOfCyborgsToSendToTakeFactory(originFactory, targetFactory);
            if(required < 0)
                return;

            if(required > availableCyborgs && targetFactory.ownerIsNone())
                return;

            sendOrder(new Order(originFactory, targetFactory, Math.min(availableCyborgs, required)));
        }
    }

    private void sendOrder(Order order) {

        if(!order.isBomb() && order.getNum() < 1)
            return;

        order.getFrom().setCyborgs(order.getFrom().getNumCyborgs() - order.getNum());

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

        int sumFromArriving = getSumOfArrivingTroops(toFactory);

        if(toFactory.ownerIsNone()) {

            return toFactory.getNumCyborgs() + sumFromArriving + 1;

        } else if(toFactory.ownerIsEnemy()) {

            int distance = fromFactory.distanceTo(toFactory);
            return toFactory.getNumCyborgs() + toFactory.getProduction() * distance + sumFromArriving + 1;

        } else {
            System.err.println("Tried to calculate number of cyborgs needed to overtake your own factory");
            System.exit(-1);
            return -1;
        }
    }


    private int getSumOfArrivingTroops(Factory activeFactory) {

        List<Troop> troopsHeadingToPlanet = Stream.of(gameState.getOutGoingTroops().stream(), gameState.getTroops().stream())
                .flatMap(t -> t)
                .filter(troop -> troop.getTargetFactory().equals(activeFactory))
                .collect(Collectors.toList());

        int balance = 0;
        for(Troop t : troopsHeadingToPlanet) {
            if(t.ownerIsMe()) {
                balance += t.getNumCyborgs();

            } else {
                balance -= t.getNumCyborgs();
            }
        }

        return balance;
    }

}
