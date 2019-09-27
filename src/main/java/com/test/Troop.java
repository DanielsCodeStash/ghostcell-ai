package com.test;

public class Troop {

	private int id;
	private Owner owner;
	private int leavingFactory;
	private int targetFactory;
	private int numCyborgs;
	private int turnsUntilArrival;

	public Owner getOwner() {
		return owner;
	}

	public int getId() {
		return id;
	}

	public Troop setId(int id) {
		this.id = id;
		return this;
	}

	public Troop setOwner(Owner owner) {
		this.owner = owner;
		return this;
	}

	public int getLeavingFactory() {
		return leavingFactory;
	}

	public Troop setLeavingFactory(int leavingFactory) {
		this.leavingFactory = leavingFactory;
		return this;
	}

	public int getTargetFactory() {
		return targetFactory;
	}

	public Troop setTargetFactory(int targetFactory) {
		this.targetFactory = targetFactory;
		return this;
	}

	public int getNumCyborgs() {
		return numCyborgs;
	}

	public Troop setNumCyborgs(int numCyborgs) {
		this.numCyborgs = numCyborgs;
		return this;
	}

	public int getTurnsUntilArrival() {
		return turnsUntilArrival;
	}

	public Troop setTurnsUntilArrival(int turnsUntilArrival) {
		this.turnsUntilArrival = turnsUntilArrival;
		return this;
	}

	@Override
	public String toString() {
		return "Troop{" +
				"id=" + id +
				", owner=" + owner +
				", leavingFactory=" + leavingFactory +
				", targetFactory=" + targetFactory +
				", numCyborgs=" + numCyborgs +
				", turnsUntilArrival=" + turnsUntilArrival +
				'}';
	}
}
