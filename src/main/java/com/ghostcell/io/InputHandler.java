package com.ghostcell.io;

import com.ghostcell.GameState;
import com.ghostcell.container.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputHandler {

    public static void readInitInput(GameState gameState, Scanner in) {
        int factoryCount = in.nextInt(); // the number of factories
        int linkCount = in.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            int factory1 = in.nextInt();
            int factory2 = in.nextInt();
            int distance = in.nextInt();

            gameState.addLink(new Link(factory1, factory2, distance));
        }
    }

    public static void readTurnInput(GameState gameState, Scanner in) {
        List<Troop> troops = new ArrayList<>();
        List<Bomb> bombs = new ArrayList<>();

        int entityCount = in.nextInt();
        for (int i = 0; i < entityCount; i++) {
            int entityId = in.nextInt();
            String entityType = in.next();
            int arg1 = in.nextInt();
            int arg2 = in.nextInt();
            int arg3 = in.nextInt();
            int arg4 = in.nextInt();
            int arg5 = in.nextInt();

            if (entityType.equals("TROOP")) {
                Troop troop = new Troop()
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setLeavingFactory(arg2)
                        .setTargetFactory(arg3)
                        .setNumCyborgs(arg4)
                        .setTurnsUntilArrival(arg5);
                troops.add(troop);

            } else if (entityType.equals("FACTORY")) {
                gameState.getFactoryById(entityId)
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setCyborgs(arg2)
                        .setProduction(arg3)
                        .setTurnsUntilProduction(arg4);

            } else if (entityType.equals("BOMB")) {
                Bomb bomb = new Bomb()
                        .setId(entityId)
                        .setOwner(Owner.fromId(arg1))
                        .setLeavingFactory(arg2)
                        .setTargetFactory(arg3)
                        .setTurnsBeforeExplosion(arg4);
                bombs.add(bomb);

            } else {
                System.err.println("Unknown entity");
            }
        }

        gameState
                .setBombs(bombs)
                .setTroops(troops);
    }
}
