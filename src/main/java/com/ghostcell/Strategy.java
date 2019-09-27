package com.ghostcell;

import com.ghostcell.container.Factory;
import com.ghostcell.container.Order;
import com.ghostcell.container.Owner;

import java.util.ArrayList;
import java.util.List;

public abstract class Strategy {

	protected List<Factory> myFactories;
    protected List<Factory> enemyFactories;
    protected List<Factory> neutralFactories;


    protected List<Order> orders;

    protected GameState gameState;

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
