package com.xenonmolecule.battlecomp.bot.Botholomew;

import com.xenonmolecule.battlecomp.bot.Bot;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.BoardScanner;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.HitMap;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.MapPreProcessor;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships.AircraftCarrrierSunkShip;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships.StandardSunkShip;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships.SubmarineSunkShip;
import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships.SunkShip;
import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.game.ships.hit.HitShip;
import com.xenonmolecule.battlecomp.game.ships.map.MapShip;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.io.GameState;
import com.xenonmolecule.battlecomp.io.PlacedShip;

import java.util.ArrayList;
import java.util.List;

public class Botholomew extends Bot {

    ArrayList<HitShip> unsunk = new ArrayList<HitShip>();
    ArrayList<SunkShip> sunk = new ArrayList<SunkShip>();

    public Botholomew(Map map) {
        super(map);
        unsunk.add(HitShip.PATROL);
        unsunk.add(HitShip.CRUISER);
        unsunk.add(HitShip.BATTLESHIP);
        unsunk.add(HitShip.SUBMARINE);
        unsunk.add(HitShip.AIRCRAFT_CARRIER);
    }

    public Botholomew() {
        super();
        unsunk.add(HitShip.PATROL);
        unsunk.add(HitShip.CRUISER);
        unsunk.add(HitShip.BATTLESHIP);
        unsunk.add(HitShip.SUBMARINE);
        unsunk.add(HitShip.AIRCRAFT_CARRIER);
    }

    /////////////////////////////////////////////////////////////////////
    //
    //                          BOT METHODS
    //
    /////////////////////////////////////////////////////////////////////

    @Override
    public Coordinate autoTakeTurn() {
        BoardScanner attacker = new BoardScanner(new HitMap(getOppMap()));
        Coordinate bestShot = attacker.getBestShot(unsunk);
        System.out.println("(" + bestShot.getX() + "," + bestShot.getY() +")");
        return bestShot;
    }

    @Override
    public Map autoPlaceShips() {
        Placer botholomewPlacer = new Placer();
        return botholomewPlacer.generateBoard();
    }

    /////////////////////////////////////////////////////////////////////
    //
    //                         CLIENT METHODS
    //
    /////////////////////////////////////////////////////////////////////

    @Override
    public Coordinate takeTurn() {
        return autoTakeTurn();
    }

    @Override
    public List<PlacedShip> placeShips() {
        List<MapShip> ships = autoPlaceShips().getShips();
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

    @Override
    public void onShipSunk(String shipType, int x, int y, int orientation) {
        SunkShip ship;
        try {
            switch (shipType) {
                case "patrol":
                    ship = new StandardSunkShip("Patrol", 2, x, y, orientation);
                    unsunk.remove(HitShip.PATROL);
                    break;
                case "cruiser":
                    ship = new StandardSunkShip("Cruiser", 3, x, y, orientation);
                    unsunk.remove(HitShip.CRUISER);
                    break;
                case "battleship":
                    ship = new StandardSunkShip("Battleship", 4, x, y, orientation);
                    unsunk.remove(HitShip.BATTLESHIP);
                    break;
                case "submarine":
                    ship = new SubmarineSunkShip(x, y, orientation);
                    unsunk.remove(HitShip.SUBMARINE);
                    break;
                case "aircraft carrier":
                    ship = new AircraftCarrrierSunkShip(x, y, orientation);
                    unsunk.remove(HitShip.AIRCRAFT_CARRIER);
                    break;
                default:
                    System.out.println("This isn't a regular ship type...");
                    ship = new StandardSunkShip("Patrol", 2, x, y, orientation);
            }
            sunk.add(ship);
        } catch (MisplacedShipException e) {
            System.out.println("\n\nI am going to keep running, but a major error just got sent by the server giving an invalid position of a sunk ship...\n\n");
        }
    }

    @Override
    public void onMapUpdate(int[][] map) {
        MapPreProcessor precheck = new MapPreProcessor(map, sunk);
        setOppMap(precheck.preprocess());
    }

    @Override
    public void onOppMapUpdate(int[][] map) {
        //TODO: DISPLAY OPPONENT MAP LATER IF I WANT
    }

    @Override
    public void onGamestateUpdate(GameState state) {
        //TODO: STUFF LATER IF I WANT
    }
}
