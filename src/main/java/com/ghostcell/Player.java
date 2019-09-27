package com.ghostcell;

import com.ghostcell.container.Order;
import com.ghostcell.io.InputHandler;
import com.ghostcell.io.OutputHandler;

import java.util.*;

@SuppressWarnings("InfiniteLoopStatement")
class Player {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		GameState gameState = GameState.init();

		Strategy strategy = new PrioritizationStrategy(gameState);

		InputHandler.readInitInput(gameState, in);


		while (true) {

			InputHandler.readTurnInput(gameState, in);

			List<Order> orders = strategy.run(gameState);

			OutputHandler.executeOrders(orders);

			gameState.turnDone();
		}
	}
}