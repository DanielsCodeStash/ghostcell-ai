package com.ghostcell.stategy;

import com.ghostcell.GameState;
import com.ghostcell.Strategy;
import com.ghostcell.container.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrioritizationStrategy extends Strategy {

    private PrioritizationModel prioModel;

    private BombPrioritizationModel bombPrioModel;

    public PrioritizationStrategy(GameState gameState) {
        super(gameState);
        prioModel = new PrioritizationModel(gameState);
        bombPrioModel = new BombPrioritizationModel(gameState);
    }

    @Override
    public List<Order> run(GameState gameData) {
        initRound();

        for(Factory activeFactory : myFactories) {

            for(Factory targetFactory : prioModel.getPrioList(activeFactory)) {
                evaluateAction(activeFactory, targetFactory);
            }

            for(FactoryPrio prio : bombPrioModel.getPrioList(activeFactory)) {
                evaluateBombAction(activeFactory, prio.getTargetFactory());
            }
        }

        return orders;
    }

    private void evaluateBombAction(Factory activeFactory, Factory targetFactory) {
        if(gameState.getNumBombsRemaining() == 0)
            return;

        if(activeFactory.ownerIsMe() && targetFactory.ownerIsEnemy()) {
            if(targetFactory.getBombPrio() > 0.35) {
                Order bomb = new Order()
                        .setFrom(activeFactory)
                        .setTo(targetFactory)
                        .setBomb(true);

                orders.add(bomb);
                gameState.removeOneAvailableBomb();
            }
        }
    }

    private void evaluateAction(Factory activeFactory, Factory targetFactory) {

        if(activeFactory.getId() == targetFactory.getId())
            return;

        int availableCyborgs = activeFactory.getNumCyborgs() + getSumOfArrivingTroops(activeFactory);
        if(availableCyborgs <= 1)
            return;

        if(targetFactory.ownerIsMe()) {

            int balance = targetFactory.getNumCyborgs() + getSumOfArrivingTroops(targetFactory);
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
