package com.ghostcell;

import com.ghostcell.container.Factory;
import com.ghostcell.container.Owner;

import java.util.List;

public class Ai {

	List<Factory> ownFactories;
	List<Factory> enemyFactories;
	List<Factory> neutralFactories;

	List<Factory> future;

	public void init(GameData gameData) {
		ownFactories = gameData.getFactoryListByOwner(Owner.YOU);
		enemyFactories = gameData.getFactoryListByOwner(Owner.ENEMY);
		neutralFactories = gameData.getFactoryListByOwner(Owner.NEUTRAL);
	}


	public void run(GameData gameData) {

	}
}
