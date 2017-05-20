package com.xenonmolecule.battlecomp.game.ships;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class SubmarineShip extends MapShip {

    public SubmarineShip(int x, int y, int orientation) {
        super("Submarine",4,x,y,orientation);
    }

    @Override
    public void place(Map map) throws MisplacedShipException {
        int orientation = getOrientation();
        int x = getX();
        int y = getY();
        List<Coordinate> coords = new ArrayList<Coordinate>();

        badThrowerPlacer(x,y,1.0,map);
        coords.add(new Coordinate(x,y));
        if(orientation != 0) {
            badThrowerPlacer(x, y - 1, 1.0, map);
            coords.add(new Coordinate(x,y-1));
        }
        if(orientation != 1) {
            badThrowerPlacer(x-1,y, 1.0,map);
            coords.add(new Coordinate(x-1,y));
        }
        if(orientation != 2) {
            badThrowerPlacer(x,y+1, 1.0,map);
            coords.add(new Coordinate(x,y+1));
        }
        if(orientation != 3){
            badThrowerPlacer(x+1,y, 1.0,map);
            coords.add(new Coordinate(x+1,y));
        }

        for(Coordinate coord : coords)
            badThrowerPlacer(coord.getX(), coord.getY(), 0.0, map);

        surroundPoint(coords.get(1).getX(), coords.get(1).getY(), map);
        surroundPoint(coords.get(2).getX(), coords.get(2).getY(), map);
        surroundPoint(coords.get(3).getX(), coords.get(3).getY(), map);

        setCoords(coords);
    }

    private void badThrowerPlacer(int x, int y, double val, Map map) throws MisplacedShipException {
        if (!map.setCoord(x,y,val))
            throw new MisplacedShipException(x,y, "Submarine - Unknown");
    }
}
