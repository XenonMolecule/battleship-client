package com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships;

import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public abstract class SunkShip {

    private String name;
    private int length, x, y, orientation;
    List<Coordinate> coords = new ArrayList<Coordinate>();

    public SunkShip(String name, int length, int x, int y, int orientation) {
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

}
