package com.ghostcell;

import com.ghostcell.container.Factory;
import com.ghostcell.container.Order;
import com.ghostcell.container.Owner;

import java.util.ArrayList;
import java.util.List;

public abstract class Strategy {

	List<Factory> myFactories;
	List<Factory> enemyFactories;
	List<Factory> neutralFactories;


	List<Order> orders;

	GameState gameState;

	public Strategy(GameState gameState) {
		this.gameState = gameState;
	}


	public void initRound() {


		myFactories = gameState.getFactoryListByOwner(Owner.YOU);
		enemyFactories = gameState.getFactoryListByOwner(Owner.ENEMY);
		neutralFactories = gameState.getFactoryListByOwner(Owner.NEUTRAL);

		gameState.setOutGoingTroops(new ArrayList<>());
		orders = new ArrayList<>();
	}


	public abstract List<Order> run(GameState gameData);
}
