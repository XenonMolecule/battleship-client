package com.xenonmolecule.battlecomp.bot.Botholomew.attacking;

import com.xenonmolecule.battlecomp.bot.Botholomew.attacking.ships.SunkShip;
import com.xenonmolecule.battlecomp.io.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class MapPreProcessor {

    int[][] map;
    List<SunkShip> ships;

    public MapPreProcessor(int[][] map, List<SunkShip> ships) {
        this.map = map;
        this.ships = ships;
    }

    public int[][] preprocess() {
        for(SunkShip ship : ships) {
            for(Coordinate coord : ship.getCoords()) {
                map[coord.getY()][coord.getX()] = -1;
            }
        }
        for(int i = 0; i < map.length; i ++) {
            for(int j= 0; j < map[i].length; j ++) {
                if(map[i][j] == 2) {
                    map[i][j] = -1;
                }
            }
        }
        return map;
    }

    public int[][] preprocessGraphics() {
        for(SunkShip ship : ships) {
            for(Coordinate coord : ship.getCoords()) {
                map[coord.getY()][coord.getX()] = 3;
            }
        }
        return map;
    }

}
