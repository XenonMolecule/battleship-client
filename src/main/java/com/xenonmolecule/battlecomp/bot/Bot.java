package com.xenonmolecule.battlecomp.bot;

import com.xenonmolecule.battlecomp.game.Map;

public abstract class Bot {

    Map map;

    public Bot(Map map) {
        this.map = map;
    }

    public Bot() {
        this.map = new Map();
    }

    public abstract void takeTurn();

    public abstract Map placeShips();

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
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
