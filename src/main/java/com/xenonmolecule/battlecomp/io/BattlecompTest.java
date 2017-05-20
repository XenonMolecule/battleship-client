package com.xenonmolecule.battlecomp.io;

import java.util.ArrayList;
import java.util.List;

public class BattlecompTest extends BattlecompClient {
    private static int x = 0;

    public static void main(String[] args) {

        BattlecompClient conn2 = new BattlecompTest() {
            @Override
            public void onGamestateUpdate(GameState state) {
                // TODO Whatever tbh, nothing really required here
            }

            @Override
            public Coordinate takeTurn() {
                if (x > 9)
                    x = 0;
                return new Coordinate(0, x++); // TODO Implement
            }

            @Override
            public List<PlacedShip> placeShips() {
                ArrayList<PlacedShip> ships = new ArrayList<>();
                PlacedShip patrol = new PlacedShip("patrol", 2, 0, 0, 0);
                ships.add(patrol);
                PlacedShip cruiser = new PlacedShip("cruiser", 3, 0, 1, 0);
                ships.add(cruiser);
                PlacedShip battleship = new PlacedShip("battleship", 4, 0, 2, 0);
                ships.add(battleship);
                PlacedShip submarine = new PlacedShip("submarine", 5, 5, 0);
                ships.add(submarine);
                PlacedShip carrier = new PlacedShip("aircraft carrier", 5, 8, 0);
                ships.add(carrier);
                return ships;
            }
        };

        BattlecompClient conn1 = new BattlecompTest() {
            @Override
            public void onGamestateUpdate(GameState state) {
                // TODO Whatever tbh, nothing really required here
                if (state == GameState.JOINED)
                    if (conn2.getGameId() == 0)
                        conn2.joinGame(getGameId());
            }

            @Override
            public Coordinate takeTurn() {
                return new Coordinate(0, 0); // TODO Implement
            }

            @Override
            public List<PlacedShip> placeShips() {
                ArrayList<PlacedShip> ships = new ArrayList<>();
                PlacedShip patrol = new PlacedShip("patrol", 2, 0, 0, 0);
                ships.add(patrol);
                PlacedShip cruiser = new PlacedShip("cruiser", 3, 0, 1, 0);
                ships.add(cruiser);
                PlacedShip battleship = new PlacedShip("battleship", 4, 0, 2, 0);
                ships.add(battleship);
                PlacedShip submarine = new PlacedShip("submarine", 5, 5, 0);
                ships.add(submarine);
                PlacedShip carrier = new PlacedShip("aircraft carrier", 5, 8, 0);
                ships.add(carrier);
                return ships;
            }
        };
        conn1.connect("http://localhost:8080");
        System.out.println("Conn 1:");
        conn1.getId();
        conn2.connect("http://localhost:8080");
        System.out.println("Conn 2:");
        conn2.getId();
        System.out.println("Creating game...");
        conn1.createGame();
    }

    @Override
    public void onGamestateUpdate(GameState state) {
        // TODO: STUFF LATER IF I WANT
    }

    @Override
    public Coordinate takeTurn() {
        return null;
    }

    @Override
    public List<PlacedShip> placeShips() {
        return null;
    }

    @Override
    public void onShipSunk(String shipType, int x, int y, int orientation) {

    }

    @Override
    public void onMapUpdate(int[][] map) {

    }

    @Override
    public void onOppMapUpdate(int[][] map) {

    }
}