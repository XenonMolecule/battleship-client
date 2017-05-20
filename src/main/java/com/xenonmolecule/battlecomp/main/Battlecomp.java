package com.xenonmolecule.battlecomp.main;

import com.xenonmolecule.battlecomp.io.BattlecompClient;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.io.GameState;
import com.xenonmolecule.battlecomp.io.PlacedShip;

import java.util.ArrayList;
import java.util.List;

public class Battlecomp extends BattlecompClient {
    private final String SERVER_IP = "http://localhost:8080";

    Battlecomp() {
        // Connect to the server
        connect(SERVER_IP);
        // If hosting: createGame();
        // If joining: joinGame(int gameID);
    }

    /*
    ** Methods for controlling battlecomp
     */

    @Override
    public void onGamestateUpdate(GameState state) {
        // TODO Handle game states
    }

    @Override
    public Coordinate takeTurn() {
        // TODO Return coordinate to attack
        return new Coordinate(0, 0);
    }

    @Override
    public List<PlacedShip> placeShips() {
        // TODO Figure out where we want to place our ships
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