package com.ghostcell;

import java.util.*;

class Player {

	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);

		GameData gameData = new GameData();
		Ai ai = new PrioritizationStrategy();

		gameData.parseLinkData(in, gameData);


		// game loop
		while (true) {

			gameData.turnStart(in);

			ai.run(gameData);

			gameData.endTurn();
		}
	}
}