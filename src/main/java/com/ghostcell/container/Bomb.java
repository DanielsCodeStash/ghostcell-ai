package com.ghostcell.container;

public class Bomb {

    private int id;
    private Owner owner;
    private int leavingFactory;
    private int targetFactory;
    private int turnsBeforeExplosion;


    public Owner getOwner() {
        return owner;
    }

    public Bomb setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public int getId() {
        return id;
    }

    public Bomb setId(int id) {
        this.id = id;
        return this;
    }

    public int getLeavingFactory() {
        return leavingFactory;
    }

    public Bomb setLeavingFactory(int leavingFactory) {
        this.leavingFactory = leavingFactory;
        return this;
    }

    public int getTargetFactory() {
        return targetFactory;
    }

    public Bomb setTargetFactory(int targetFactory) {
        this.targetFactory = targetFactory;
        return this;
    }

    public int getTurnsBeforeExplosion() {
        return turnsBeforeExplosion;
    }

    public Bomb setTurnsBeforeExplosion(int turnsBeforeExplosion) {
        this.turnsBeforeExplosion = turnsBeforeExplosion;
        return this;
    }
}
