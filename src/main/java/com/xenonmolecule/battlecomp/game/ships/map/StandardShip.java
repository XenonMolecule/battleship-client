package com.xenonmolecule.battlecomp.game.ships.map;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.io.Coordinate;

public class StandardShip extends MapShip {


    public StandardShip(String name, int length, int x, int y, int orientation) {
        super(name,length,x,y,orientation);
    }

    @Override
    public void place(Map map) throws MisplacedShipException {
        int length = getLength();
        int orientation = getOrientation();
        int x = getX();
        int y = getY();
        getCoords().clear();

        for(int i = 0; i < length; i ++) {
            if(x < map.MAP_WIDTH && y < map.MAP_HEIGHT) {
                if(map.getCoord(x,y) != 0.0) {
                    addCoord(x,y);
                    switch(orientation) {
                        case 0:
                            x++;
                            break;
                        case 1:
                            y--;
                            break;
                        case 2:
                            x--;
                            break;
                        case 3:
                            y++;
                            break;
                        default:
                            System.out.println("OH NOES... Bad Orientation");
                    }
                } else {
                    MisplacedShipException badShip = new MisplacedShipException(x,y, "Ship already placed in this position");
                    getCoords().clear();
                    throw badShip;
                }
            } else {
                MisplacedShipException badShip = new MisplacedShipException(x,y, "Out of Map Bounds");
                getCoords().clear();
                throw badShip;
            }
        }
        for(Coordinate coord : getCoords()) {
            if(!map.setCoord(coord.getX(), coord.getY(),0.0)) {
                MisplacedShipException badShip = new MisplacedShipException(coord.getX(),coord.getY(), "Unknown");
                getCoords().clear();
                throw badShip;
            }
        }

        // Surrounds ship of length < 6 with buffer points
        surroundPoint(getCoords().get(0).getX(),getCoords().get(0).getY(), map);
        surroundPoint(getCoords().get(getCoords().size()-1).getX(),getCoords().get(getCoords().size()-1).getX(), map);

    }

}
