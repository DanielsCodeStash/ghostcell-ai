package com.test;

public enum Owner {
	YOU, ENEMY, NEUTRAL;

	public static Owner fromId(int id) {
		switch (id) {
			case 1: return YOU;
			case 0: return NEUTRAL;
			case -1: return ENEMY;
		}
		return null;
	}
}
