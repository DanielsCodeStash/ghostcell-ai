package com.test;

public class Link {
	private int factory1;
	private int factory2;
	private int distance;

	public Link(int factory1, int factory2, int distance) {
		this.factory1 = factory1;
		this.factory2 = factory2;
		this.distance = distance;
	}


	public int getFactory1() {
		return factory1;
	}

	public Link setFactory1(int factory1) {
		this.factory1 = factory1;
		return this;
	}

	public int getFactory2() {
		return factory2;
	}

	public Link setFactory2(int factory2) {
		this.factory2 = factory2;
		return this;
	}

	public int getDistance() {
		return distance;
	}

	public Link setDistance(int distance) {
		this.distance = distance;
		return this;
	}

	@Override
	public String toString() {
		return "Link{" +
				"factory1=" + factory1 +
				", factory2=" + factory2 +
				", distance=" + distance +
				'}';
	}
}
