package com.ghostcell;

import com.ghostcell.container.*;

import java.util.*;


public class GameState {

    private int turnNumber;

    private List<Link> links = new ArrayList<>();
    private List<Troop> troops;
    private List<Factory> factories = new ArrayList<>();
    private List<Troop> outGoingTroops = new ArrayList<>();
    private List<Bomb> bombs;

    private Map<Integer, Factory> idToFactory = new HashMap<>();
    private Map<String, Integer> factoryIdsToDistance = new HashMap<>();


    private static GameState instance;
    public static GameState getInstance() {
        return instance;
    }

    public static GameState init() {
        instance = new GameState();
        instance.setTurnNumber(0);
        return instance;
    }

    public GameState setOutGoingTroops(List<Troop> outGoingTroops) {
        this.outGoingTroops = outGoingTroops;
        return this;
    }

    public List<Factory> getFactoryListByOwner(Owner owner) {
        List<Factory> outFactories = new ArrayList<>();
        for (Factory f : factories) {
            if (f.getOwner() == owner) {
                outFactories.add(f);
            }
        }
        return outFactories;
    }

    public void turnDone() {
        turnNumber++;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public GameState setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
        return this;
    }

    public Factory getFactoryById(int entityId) {
        Factory factory = idToFactory.get(entityId);
        if(factory == null) {
            factory = new Factory()
                    .setId(entityId);
            factories.add(factory);
            idToFactory.put(entityId, factory);
        }
        return factory;
    }

    public List<Troop> getOutGoingTroops() {
        return outGoingTroops;
    }

    public void addOutgoingTroop(Troop troop) {
        outGoingTroops.add(troop);
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

        if(factories != null) {
            for (Factory f : factories)
                System.err.println(f);
        }

        if(troops != null) {
            for (Troop t : troops)
                System.err.println(t);
        }


        if(links != null) {
            for (Link l : links)
                System.err.println(l);
        }

        System.err.println("===============");
    }

    public GameState setLinks(List<Link> links) {
        this.links = links;
        return this;
    }

    public GameState setTroops(List<Troop> troops) {
        this.troops = troops;
        return this;
    }

    public GameState setFactories(List<Factory> factories) {
        this.factories = factories;
        return this;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public GameState setBombs(List<Bomb> bombs) {
        this.bombs = bombs;
        return this;
    }


    public int distanceBetweenFactories(Factory from, Factory to) {
        if (from.getId() == to.getId()) {
            return 0;
        }

        String key = from.getId() + " -> " + to.getId();

        Integer distance = factoryIdsToDistance.get(key);
        if(distance == null) {
            distance = getDistanceFromLinks(from, to);
            factoryIdsToDistance.put(key, distance);
        }

        if(distance == null) {
            System.err.println("Failed to find distance, something is wrong");
            System.exit(-1);
        }

        return distance;
    }

    private Integer getDistanceFromLinks(Factory from, Factory to) {
        for (Link link : links) {
            if (link.getFactory1() == from.getId() && link.getFactory2() == to.getId()) {
                return link.getDistance();
            } else if (link.getFactory1() == to.getId() && link.getFactory2() == from.getId()) {
                return link.getDistance();
            }
        }
        return null;
    }

}
