package com.test;

import java.util.ArrayList;
import java.util.List;

public class Aa_2 extends Ai {


	@Override
	public void run(GameData gameData) {
		init(gameData);

		List<Factory> factoriesWhereImNotFutureOwner = new ArrayList<Factory>();

		for(Factory f : gameData.getFactories()) {

			if(f.getFutureOwner() != Owner.YOU)
				factoriesWhereImNotFutureOwner.add(f);

		}

		int maxOwnCyborgs = 0;
		Factory factoryWhereIHaveMostCyborgs = null;
		for(Factory f : ownFactories) {
			if(f.getCyborgs() > maxOwnCyborgs) {
				maxOwnCyborgs = f.getCyborgs();
				factoryWhereIHaveMostCyborgs = f;
			}
		}

		if(maxOwnCyborgs < 5)
			return;

		Factory closestNotOwner = null;
		int shortestDistance = 325435;
		for(Factory f : factoriesWhereImNotFutureOwner) {

			int distance = gameData.distanceBetweenFactories(factoryWhereIHaveMostCyborgs, f, gameData);
			if(distance < shortestDistance) {
				closestNotOwner = f;
				shortestDistance = distance;
			}
		}



		int troopsToSend = factoryWhereIHaveMostCyborgs.getCyborgs() - 5;
		gameData.addOrder(new Order(factoryWhereIHaveMostCyborgs, closestNotOwner, troopsToSend));

	}

}
