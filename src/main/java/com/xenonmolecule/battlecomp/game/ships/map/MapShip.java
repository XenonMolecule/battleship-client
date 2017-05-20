package com.xenonmolecule.battlecomp.game.ships.map;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public abstract class MapShip {

    public static final double BUFFER_MOD = 0.1; // was 0.25, but 0.1 separates ships even better

    private String name;
    private int length, x, y, orientation;
    List<Coordinate> coords = new ArrayList<Coordinate>();

    public MapShip(String name, int length, int x, int y, int orientation) {
        this.name = name;
        this.length = length;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrientation() {
        return orientation;
    }

    public void addCoord(int x, int y) {
        coords.add(new Coordinate(x,y));
    }

    public List<Coordinate> getCoords() {
        return coords;
    }

    public void setCoords(List<Coordinate> coordinates) {
        coords = coordinates;
    }

    public abstract void place(Map map) throws MisplacedShipException;

    // Surrounds point with buffer points
    public void surroundPoint(int x, int y, Map map) {
        for(int i = -2; i < 3; i ++) {
            for(int j = -2; j < 3; j ++) {
                map.setCoord(x+i,y+j,BUFFER_MOD);
            }
        }
    }

    @Override
    public String toString() {
        return name + " - (" + x + "," + y + "," + orientation + ")";
    }

}
