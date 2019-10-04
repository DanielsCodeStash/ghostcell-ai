package com.ghostcell.container;

import com.ghostcell.GameState;

public class Factory {

	private int id;
	private Owner owner;
	private int cyborgs;

	private double factoryPrio;
	private double bombPrio;

	private int production;

	private int turnsUntilProduction;

	public Factory() {
	}

	public int distanceTo(Factory otherFactory) {
		return GameState.getInstance().distanceBetweenFactories(this, otherFactory);
	}

	public boolean ownerIsMe() {
		return owner == Owner.YOU;
	}

	public boolean ownerIsEnemy() {
		return owner == Owner.ENEMY;
	}

	public boolean ownerIsNone() {
		return owner == Owner.NEUTRAL;
	}


	public int getTurnsUntilProduction() {
		return turnsUntilProduction;
	}


	public Factory setTurnsUntilProduction(int turnsUntilProduction) {
		this.turnsUntilProduction = turnsUntilProduction;
		return this;
	}

	public double getFactoryPrio() {
		return factoryPrio;
	}

	public Factory setFactoryPrio(double factoryPrio) {
		this.factoryPrio = factoryPrio;
		return this;
	}

	public double getBombPrio() {
		return bombPrio;
	}

	public Factory setBombPrio(double bombPrio) {
		this.bombPrio = bombPrio;
		return this;
	}

	public int getId() {
		return id;
	}

	public Factory setId(int id) {
		this.id = id;
		return this;
	}

	public Owner getOwner() {
		return owner;
	}

	public Factory setOwner(Owner owner) {
		this.owner = owner;
		return this;
	}

	public int getNumCyborgs() {
		return cyborgs;
	}

	public Factory setCyborgs(int cyborgs) {
		this.cyborgs = cyborgs;
		return this;
	}

	public int getProduction() {
		return production;
	}

	public Factory setProduction(int production) {
		this.production = production;
		return this;
	}

	@Override
	public String toString() {
		return "Factory{" +
				"id=" + id +
				", owner=" + owner +
				", cyborgs=" + cyborgs +
				", factoryPrio=" + factoryPrio +
				", bombPrio=" + bombPrio +
				", production=" + production +
				", turnsUntilProduction=" + turnsUntilProduction +
				'}';
	}
}
