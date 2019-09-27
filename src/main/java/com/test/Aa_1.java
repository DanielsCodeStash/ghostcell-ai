package com.test;

public class Aa_1 extends Ai {



	public void run(GameData gameData) {
		init(gameData);

		int totalTroops = 0;
		for(Factory f : ownFactories) {
			totalTroops += f.getCyborgs();
		}

		int avgTroopsPerFactory = totalTroops / ownFactories.size();

		for(Factory f : ownFactories) {

			if(ownFactories.size() == 1) {

				Factory targetFactory = pickTargetFactory(f);


				Order order = new Order()
						.setFrom(f)
						.setTo(targetFactory)
						.setNum(f.getCyborgs() /2);

				gameData.addOrder(order);
				return;

			} else if(f.getCyborgs() > avgTroopsPerFactory) {

				Factory targetFactory = pickTargetFactory(f);

				int num = f.getCyborgs() - avgTroopsPerFactory;

				if(num < 10)
					continue;

				Order order = new Order()
						.setFrom(f)
						.setTo(targetFactory)
						.setNum(f.getCyborgs() - avgTroopsPerFactory);

				gameData.addOrder(order);
				return;
			}
		}
	}

	private Factory pickTargetFactory(Factory f) {
		Factory targetFactory = null;

		if(!neutralFactories.isEmpty()) {
			targetFactory = neutralFactories.get(0);
		}

		if(targetFactory == null && !enemyFactories.isEmpty()) {
			targetFactory = enemyFactories.get(0);
		}

		return targetFactory;
	}
}
