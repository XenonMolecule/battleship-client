package com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;

public class StandardSunkShip extends SunkShip {

    public StandardSunkShip(String name, int length, int x, int y, int orientation) throws MisplacedShipException {
        super(name,length,x,y,orientation);
        for(int i = 0; i < length; i ++) {
            if((x < Map.MAP_WIDTH && x >= 0) && (y < Map.MAP_HEIGHT && y >= 0)) {
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
                MisplacedShipException badShip = new MisplacedShipException(x,y, "Out of Map Bounds");
                getCoords().clear();
                throw badShip;
            }
        }
    }
}
