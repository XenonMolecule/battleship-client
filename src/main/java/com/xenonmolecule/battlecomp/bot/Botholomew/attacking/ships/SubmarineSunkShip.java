package com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class SubmarineSunkShip extends SunkShip{

    public SubmarineSunkShip(int x, int y, int orientation) throws MisplacedShipException {
        super("Submarine",4,x,y,orientation);
        List<Coordinate> coords = new ArrayList<Coordinate>();

        if(inBounds(x,y)) {
            coords.add(new Coordinate(x, y));
            if (orientation != 0) {
                if (inBounds(x, y - 1)) {
                    coords.add(new Coordinate(x, y - 1));
                } else {
                    throw new MisplacedShipException(x,y-1,"Out of Map Bounds");
                }
            }

            if (orientation != 1) {
                if (inBounds(x - 1, y)){
                    coords.add(new Coordinate(x - 1, y));
                } else {
                    throw new MisplacedShipException(x-1,y,"Out of Map Bounds");
                }
            }

            if (orientation != 2) {
                if (inBounds(x, y + 1)) {
                    coords.add(new Coordinate(x, y + 1));
                } else {
                    throw new MisplacedShipException(x,y + 1,"Out of Map Bounds");
                }
            }

            if (orientation != 3) {
                if (inBounds(x + 1, y)) {
                    coords.add(new Coordinate(x + 1, y));
                } else {
                    throw new MisplacedShipException(x + 1,y,"Out of Map Bounds");
                }
            }

        }

        setCoords(coords);
    }

    private boolean inBounds(int x, int y) {
        return ((x >= 0 && x < Map.MAP_WIDTH) && (y >= 0 && y < Map.MAP_HEIGHT));
    }

}
