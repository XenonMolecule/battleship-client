package com.xenonmolecule.battlecomp.math;

import com.xenonmolecule.battlecomp.io.Coordinate;

public class Vector {

    private int dX, dY;

    public Vector(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    public int getDX() {
        return dX;
    }

    public int getDY() {
        return dY;
    }

    public Coordinate addCoord(Coordinate coord) {
        return new Coordinate(coord.getX() + dX, coord.getY() + dY);
    }

    public Coordinate shiftCoord(Coordinate coord) {
        return new Coordinate(coord.getX() - dX, coord.getY() - dY);
    }

    public Vector rotate90(int orientation) {
        int x = dX;
        int y = dY;
        for(int i = 0 ; i < orientation; i ++) {
            int temp = x;
            x = y;
            y = -1 * temp;
        }
        return new Vector(x,-1 * y);
    }
}
