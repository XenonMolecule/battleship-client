package com.xenonmolecule.battlecomp.bot;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.io.BattlecompClient;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.io.GameState;

public abstract class Bot extends BattlecompClient{

    Map map;
    private int[][] oppMap;

    public Bot(Map map) {
        this.map = map;
        this.oppMap = fillArr(new int[Map.MAP_HEIGHT][Map.MAP_WIDTH], 1);
    }

    public Bot() {
        this.map = new Map();
        this.oppMap = fillArr(new int[Map.MAP_HEIGHT][Map.MAP_WIDTH], 1);
    }

    public abstract Coordinate autoTakeTurn();

    public abstract Map autoPlaceShips();

    public Map getMap() {
        return map;
    }

    public int[][] getOppMap() {
        return oppMap;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setOppMap(int[][] map) {
        this.oppMap = oppMap;
    }

    private int[][] fillArr(int[][] arr, int val) {
        for(int i = 0; i < arr.length; i ++) {
            for (int j = 0; j < arr[i].length; j ++) {
                arr[i][j] = val;
            }
        }
        return arr;
    }

}
