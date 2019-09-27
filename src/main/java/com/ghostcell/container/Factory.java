package com.ghostcell.container;

import com.ghostcell.GameState;

public class  Factory {

	private int id;
	private Owner owner;
	private int cyborgs;

	private int futureCyborgs; // positive for current, minus for other
	private Owner futureOwner;

	private double prioWeight;

	private int production;

	private int turnsUntilProduction;

	public Factory() {
	}

	public int distanceTo(Factory otherFactory) {
		return  GameState.getInstance().distanceBetweenFactories(this, otherFactory);
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

	public double getPrioWeight() {
		return prioWeight;
	}

	public int getTurnsUntilProduction() {
		return turnsUntilProduction;
	}


	public Factory setTurnsUntilProduction(int turnsUntilProduction) {
		this.turnsUntilProduction = turnsUntilProduction;
		return this;
	}

	public Factory setPrioWeight(double prioWeight) {
		this.prioWeight = prioWeight;
		return this;
	}

	public Owner getFutureOwner() {
		return futureOwner;
	}

	public Factory setFutureOwner(Owner futureOwner) {
		this.futureOwner = futureOwner;
		return this;
	}

	public int getFutureCyborgs() {
		return futureCyborgs;
	}

	public void addToFutureCyborgs(int num) {
		futureCyborgs += num;
	}

	public Factory setFutureCyborgs(int futureCyborgs) {
		this.futureCyborgs = futureCyborgs;
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
		this.futureOwner = owner;
		this.owner = owner;
		return this;
	}

	public int getCyborgs() {
		return cyborgs;
	}

	public Factory setCyborgs(int cyborgs) {
		this.cyborgs = cyborgs;
		this.setFutureCyborgs(cyborgs);
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
				", futureCyborgs=" + futureCyborgs +
				", futureOwner=" + futureOwner +
				", prioWeight=" + prioWeight +
				", production=" + production +
				'}';
	}
}
