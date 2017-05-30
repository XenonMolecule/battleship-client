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
import com.xenonmolecule.battlecomp.graphics.BattleshipBoard;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.io.GameState;
import com.xenonmolecule.battlecomp.io.PlacedShip;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Botholomew extends Bot {

    ArrayList<HitShip> unsunk = new ArrayList<HitShip>();
    ArrayList<SunkShip> sunk = new ArrayList<SunkShip>();
    BattleshipBoard ourBoard = new BattleshipBoard();
    BattleshipBoard oppBaseBoard = new BattleshipBoard();
    BattleshipBoard weighedBoard = new BattleshipBoard();

    public Botholomew(Map map, boolean graphics) {
        super(map);
        connect("http://localhost:8080");
        unsunk.add(HitShip.PATROL);
        unsunk.add(HitShip.CRUISER);
        unsunk.add(HitShip.BATTLESHIP);
        unsunk.add(HitShip.SUBMARINE);
        unsunk.add(HitShip.AIRCRAFT_CARRIER);
        if(graphics) {
            setupGraphics();
        }
    }

    public Botholomew(boolean graphics) {
        super();
        connect("http://localhost:8080");
        unsunk.add(HitShip.PATROL);
        unsunk.add(HitShip.CRUISER);
        unsunk.add(HitShip.BATTLESHIP);
        unsunk.add(HitShip.SUBMARINE);
        unsunk.add(HitShip.AIRCRAFT_CARRIER);
        if(graphics) {
            setupGraphics();
        }
    }

    public Botholomew() {
        super();
        connect("http://localhost:8080");
        unsunk.add(HitShip.PATROL);
        unsunk.add(HitShip.CRUISER);
        unsunk.add(HitShip.BATTLESHIP);
        unsunk.add(HitShip.SUBMARINE);
        unsunk.add(HitShip.AIRCRAFT_CARRIER);
    }

    void setupGraphics() {
        JFrame frame;
        frame = new JFrame("[Michael] Battleship Bot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1393, 738));
        frame.setResizable(false);
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Our basic board showing hits misses and kills
        JPanel basicBoardCont = new JPanel(new BorderLayout());
        JLabel basicBoardLabel = new JLabel("Botholomew Board:");

        ourBoard.setCells(new int[10][10]);
        basicBoardCont.add(basicBoardLabel, BorderLayout.NORTH);
        basicBoardCont.add(ourBoard, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.getContentPane().add(basicBoardCont, gbc);

        // Our board showing weighed board for our ship
        JPanel weighedBoardCont = new JPanel(new BorderLayout());
        JLabel weighedBoardLabel = new JLabel("Weighed Board:");
        weighedBoard = new BattleshipBoard();
        weighedBoard.setCells(new int[10][10]);
        weighedBoardCont.add(weighedBoardLabel, BorderLayout.NORTH);
        weighedBoardCont.add(weighedBoard, BorderLayout.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.getContentPane().add(weighedBoardCont, gbc);

        // Our opponent's basic board showing hits misses and kills
        JPanel oppBasicBoardCont = new JPanel(new BorderLayout());
        JLabel oppBasicBoardLabel = new JLabel("Opponent Board:");

        oppBaseBoard.setCells(new int[10][10]);
        oppBasicBoardCont.add(oppBasicBoardLabel, BorderLayout.NORTH);
        oppBasicBoardCont.add(oppBaseBoard, BorderLayout.CENTER);
        gbc.gridx = 2;
        gbc.gridy = 0;
        frame.getContentPane().add(oppBasicBoardCont, gbc);

        frame.pack();
        frame.setVisible(true);
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
        weighedBoard.setCells(attacker.getDisplayMapF());
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
            onMapUpdate(getOppMap());
        } catch (MisplacedShipException e) {
            System.out.println("\n\nI am going to keep running, but a major error just got sent by the server giving an invalid position of a sunk ship...\n\n");
        }
    }

    @Override
    public void onMapUpdate(int[][] map) {
        MapPreProcessor precheck = new MapPreProcessor(map, sunk);
        precheck.preprocessGraphics();
        ourBoard.setCells(map);
        setOppMap(precheck.preprocess());
    }

    @Override
    public void onOppMapUpdate(int[][] map) {
        oppBaseBoard.setCells(map);
    }

    @Override
    public void onGamestateUpdate(GameState state) {
        //TODO: STUFF LATER IF I WANT
    }
}
