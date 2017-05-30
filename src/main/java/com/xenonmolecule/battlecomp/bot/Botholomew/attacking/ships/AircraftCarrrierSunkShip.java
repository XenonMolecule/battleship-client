package com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class AircraftCarrrierSunkShip extends SunkShip {

    public AircraftCarrrierSunkShip (int x, int y, int orientation) throws MisplacedShipException {
        super("Aircraft Carrier",6,x,y,orientation);
        Coordinate currPos = new Coordinate(x,y);
        List<Coordinate> coords = new ArrayList<Coordinate>();

        if(orientation == 2) {
            currPos = new Coordinate(currPos.getX() - 3, currPos.getY() + 1);
            orientation = 0;
        } else if(orientation == 3) {
            currPos = new Coordinate(currPos.getX() + 1, currPos.getY() + 3);
            orientation = 1;
        }

        if(inBounds(currPos.getX(),currPos.getY())) {
            coords.add(currPos);
        } else {
            throw new MisplacedShipException(currPos.getX(), currPos.getY(), "Out of Map Bounds");
        }

        // I was too lazy to figure out a better way to do this than hardcoding all orientations
        if(orientation == 0) {
            currPos = placeRight(currPos.getX(), currPos.getY(), coords);
            placeUp(currPos.getX(), currPos.getY(), coords);
            currPos = placeRight(currPos.getX(), currPos.getY(), coords);
            currPos = placeUp(currPos.getX(), currPos.getY(), coords);
            placeRight(currPos.getX(), currPos.getY(), coords);
        } else {
            currPos = placeUp(currPos.getX(), currPos.getY(), coords);
            placeLeft(currPos.getX(), currPos.getY(), coords);
            currPos = placeUp(currPos.getX(), currPos.getY(), coords);
            currPos = placeLeft(currPos.getX(), currPos.getY(), coords);
            placeUp(currPos.getX(), currPos.getY(), coords);
        }

        setCoords(coords);
    }

    private boolean inBounds(int x, int y) {
        return ((x >= 0 && x < Map.MAP_WIDTH) && (y >= 0 && y < Map.MAP_HEIGHT));
    }

    private Coordinate placeRight(int x, int y, List<Coordinate> coords) throws MisplacedShipException {
        if(inBounds(x+1,y)) {
            Coordinate coord = new Coordinate(x + 1, y);
            coords.add(coord);
            return coord;
        } else {
            throw new MisplacedShipException(x+1,y,"Out of Map Bounds");
        }
    }

    private Coordinate placeUp(int x, int y, List<Coordinate> coords) throws MisplacedShipException {
        if(inBounds(x,y-1)) {
            Coordinate coord = new Coordinate(x, y-1);
            coords.add(coord);
            return coord;
        } else {
            throw new MisplacedShipException(x,y-1,"Out of Map Bounds");
        }
    }

    private Coordinate placeLeft(int x, int y, List<Coordinate> coords) throws MisplacedShipException {
        if(inBounds(x-1,y)) {
            Coordinate coord = new Coordinate(x - 1, y);
            coords.add(coord);
            return coord;
        } else {
            throw new MisplacedShipException(x,y-1,"Out of Map Bounds");
        }
    }

}
