package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GameData {

    public List<Link> links = new ArrayList<Link>();
    public List<Troop> troops;
    public List<Factory> factories;
    public List<Order> orders;
    private List<Troop> outGoingTroops;
    private List<Bomb> bombs;


    public List<Factory> getFactoryListByOwner(Owner owner) {
        List<Factory> outFactories = new ArrayList<Factory>();
        for (Factory f : factories) {
            if (f.getOwner() == owner) {
                outFactories.add(f);
            }
        }
        return outFactories;
    }

    public List<Troop> getOutGoingTroops() {
        return outGoingTroops;
    }

    public void addOutgoingTroop(Troop troop) {
        outGoingTroops.add(troop);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public List<Factory> getFactories() {
        return factories;
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void printState() {

        System.err.println("==== Round start ====");

        for (Factory f : factories)
            System.err.println(f);

        for (Troop t : troops)
            System.err.println(t);


        for (Link l : links)
            System.err.println(l);

        System.err.println("===============");
    }

    public void parseLinkData(Scanner in, GameData gameData) {
        int factoryCount = in.nextInt(); // the number of factories
        int linkCount = in.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            int factory1 = in.nextInt();
            int factory2 = in.nextInt();
            int distance = in.nextInt();

            gameData.addLink(new Link(factory1, factory2, distance));
        }
    }

    public int distanceBetweenFactories(Factory from, Factory to, GameData gameData) {
        if (from.getId() == to.getId())
            return 0;

        for (Link link : links) {
            if (link.getFactory1() == from.getId() && link.getFactory2() == to.getId()) {
                return link.getDistance();
            } else if (link.getFactory1() == to.getId() && link.getFactory2() == from.getId()) {
                return link.getDistance();
            }
        }
        System.err.println("SHOULD NOTTKDS " + from + " --- " + to);
        System.exit(0);
        gameData.printState();
        return 4534534;
    }

    public void turnStart(Scanner in) {
        troops = new ArrayList<Troop>();
        factories = new ArrayList<Factory>();
        orders = new ArrayList<Order>();
        outGoingTroops = new ArrayList<>();
        bombs = new ArrayList<>();

        int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
        for (int i = 0; i < entityCount; i++) {
            int entityId = in.nextInt();
            String entityType = in.next();
            int arg1 = in.nextInt();
            int arg2 = in.nextInt();
            int arg3 = in.nextInt();
            int arg4 = in.nextInt();
            int arg5 = in.nextInt();

            if (entityType.equals("TROOP")) {
                Troop troop = new Troop()
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setLeavingFactory(arg2)
                        .setTargetFactory(arg3)
                        .setNumCyborgs(arg4)
                        .setTurnsUntilArrival(arg5);

                troops.add(troop);
            } else if (entityType.equals("FACTORY")) {
                Factory factory = new Factory()
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setCyborgs(arg2)
                        .setProduction(arg3);
                factories.add(factory);
            } else if (entityType.equals("BOMB")) {
                Bomb bomb = new Bomb()
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setLeavingFactory(arg2)
                        .setTargetFactory(arg3)
                        .setTurnsBeforeExplosion(arg4);
                bombs.add(bomb);
            } else {
                System.err.println("Unknown entity");
            }


        }

        calculateFuture();
    }

    private void calculateFuture() {
        for (Troop t : troops) {
            Factory targetFactory = getFactoryFromId(t.getTargetFactory());
            Owner targetOwner = targetFactory.getOwner();
            Owner troopOwner = t.getOwner();
            int numTroops = t.getNumCyborgs();
            int numTroopsAtTarget = targetFactory.getFutureCyborgs();


            if ((targetOwner == Owner.YOU && troopOwner == Owner.YOU) || (targetOwner == Owner.ENEMY && troopOwner == Owner.ENEMY)) {
                targetFactory.addToFutureCyborgs(numTroops);
            } else {

                if (numTroopsAtTarget < numTroops) {
                    targetFactory.setFutureOwner(troopOwner);
                    targetFactory.setFutureCyborgs(numTroops - numTroopsAtTarget);
                } else {
                    targetFactory.setFutureCyborgs(numTroopsAtTarget - numTroops);
                }
            }
        }
    }


    private Factory getFactoryFromId(int id) {
        for (Factory f : factories) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }


    public void endTurn() {


        if (!orders.isEmpty()) {
            String out = "";
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                boolean isLast = i == orders.size() - 1;

                if(!order.isBomb()) {
                    out += "MOVE " + order.getFrom().getId() + " " + order.getTo().getId() + " " + order.getNum();
                } else {
                    out += "BOMB " + order.getFrom().getId() + " " + order.getTo().getId();
                }

                if (!isLast)
                    out += ";";
            }
            System.out.println(out);
        } else {
            System.out.println("WAIT");

        }
    }
}
