package com.ghostcell.container;

public class Order {
	private Factory from;
	private Factory to;
	private int num;
	private boolean bomb = false;
	private boolean boost = false;

	public Order() {
	}

	public Order(Factory from, Factory to, int num) {
		this.from = from;
		this.to = to;
		this.num = num;
	}

	public boolean isBoost() {
		return boost;
	}

	public Order setBoost(boolean boost) {
		this.boost = boost;
		return this;
	}

	public boolean isBomb() {
		return bomb;
	}

	public Order setBomb(boolean bomb) {
		this.bomb = bomb;
		return this;
	}

	@Override
	public String toString() {
		return "MoveOrder{" +
				"from=" + from +
				", to=" + to +
				", num=" + num +
				'}';
	}

	public Factory getFrom() {
		return from;
	}

	public Order setFrom(Factory from) {
		this.from = from;
		return this;
	}

	public Factory getTo() {
		return to;
	}

	public Order setTo(Factory to) {
		this.to = to;
		return this;
	}

	public int getNum() {
		return num;
	}

	public Order setNum(int num) {
		this.num = num;
		return this;
	}
}
