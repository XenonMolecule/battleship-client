package com.xenonmolecule.battlecomp.game.ships.map;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class AircraftCarrierShip extends MapShip {

    public AircraftCarrierShip(int x, int y, int orientation) {
        super("Aircraft Carrier",6,x,y,orientation);
    }

    @Override
    public void place(Map map) throws MisplacedShipException {
        int orientation = getOrientation();
        Coordinate currPos = new Coordinate(getX(), getY());
        List<Coordinate> coords = new ArrayList<Coordinate>();

        if(orientation == 2) {
            currPos = new Coordinate(currPos.getX() - 3, currPos.getY() + 1);
            orientation = 0;
        } else if(orientation == 3) {
            currPos = new Coordinate(currPos.getX() + 1, currPos.getY() + 3);
            orientation = 1;
        }

        badThrowerPlacer(currPos.getX(), currPos.getY(), 1.0, map);
        coords.add(currPos);

        // I was too lazy to figure out a better way to do this than hardcoding all orientations
        if(orientation == 0) {
            currPos = placeRight(currPos.getX(), currPos.getY(), coords, 1.0, map);
            placeUp(currPos.getX(), currPos.getY(), coords, 1.0, map);
            currPos = placeRight(currPos.getX(), currPos.getY(), coords, 1.0, map);
            currPos = placeUp(currPos.getX(), currPos.getY(), coords, 1.0, map);
            placeRight(currPos.getX(), currPos.getY(), coords, 1.0, map);
        } else {
            currPos = placeUp(currPos.getX(), currPos.getY(), coords, 1.0, map);
            placeLeft(currPos.getX(), currPos.getY(), coords, 1.0, map);
            currPos = placeUp(currPos.getX(), currPos.getY(), coords, 1.0, map);
            currPos = placeLeft(currPos.getX(), currPos.getY(), coords, 1.0, map);
            placeUp(currPos.getX(), currPos.getY(), coords, 1.0, map);
        }

        for(Coordinate coord : coords) {
            badThrowerPlacer(coord.getX(), coord.getY(), 0.0, map);
            surroundPoint(coord.getX(), coord.getY(), map);
        }

        setCoords(coords);
    }

    private void badThrowerPlacer(int x, int y, double val, Map map) throws MisplacedShipException {
        if (!map.setCoord(x,y,val))
            throw new MisplacedShipException(x,y, "Aircraft - Unknown");
    }

    private Coordinate placeRight(int x, int y, List<Coordinate> coords, double val, Map map) throws MisplacedShipException {
        badThrowerPlacer(x+1, y, val, map);
        Coordinate coord = new Coordinate(x+1, y);
        coords.add(coord);
        return coord;
    }

    private Coordinate placeUp(int x, int y, List<Coordinate> coords, double val, Map map) throws MisplacedShipException {
        badThrowerPlacer(x, y-1, val, map);
        Coordinate coord = new Coordinate(x, y-1);
        coords.add(coord);
        return coord;
    }

    private Coordinate placeLeft(int x, int y, List<Coordinate> coords, double val, Map map) throws MisplacedShipException {
        badThrowerPlacer(x-1, y, val, map);
        Coordinate coord = new Coordinate(x-1, y);
        coords.add(coord);
        return coord;
    }
}
