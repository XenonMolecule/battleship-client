package com.xenonmolecule.battlecomp.io;

import com.xenonmolecule.battlecomp.bot.Botholomew.Botholomew;
import com.xenonmolecule.battlecomp.bot.Botholomew.Placer;
import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.ships.map.MapShip;
import com.xenonmolecule.battlecomp.graphics.BattleshipBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattlecompTest extends BattlecompClient {
    private static int x = 0;

    public static void main(String[] args) {

        BattlecompClient conn2 = new Botholomew(true);

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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new Coordinate(0, 0); // TODO Implement
            }

            @Override
            public List<PlacedShip> placeShips() {
                Placer botholomewPlacer = new Placer();
                Map map = botholomewPlacer.generateBoard();
                List<MapShip> ships = map.getShips();
                List<PlacedShip> shipsList = new ArrayList<PlacedShip>();
                for (MapShip ship : ships) {
                    PlacedShip shipPlacement;
                    if (ship.getName().equalsIgnoreCase("Submarine") || ship.getName().equalsIgnoreCase("Aircraft Carrier")) {
                        shipPlacement = new PlacedShip(ship.getName().toLowerCase(), ship.getX(), ship.getY(), ship.getOrientation());
                    } else {
                        shipPlacement = new PlacedShip(ship.getName().toLowerCase(), ship.getLength(), ship.getX(), ship.getY(), ship.getOrientation());
                    }
                    shipsList.add(shipPlacement);
                }
                return shipsList;
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