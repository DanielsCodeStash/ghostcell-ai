package com.test;

import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);

		GameData gameData = new GameData();
		Ai ai = new Aa_3();

		gameData.parseLinkData(in, gameData);


		// game loop
		while (true) {

			gameData.turnStart(in);

			//gameData.printState();

			ai.run(gameData);


			gameData.endTurn();
		}
	}
}