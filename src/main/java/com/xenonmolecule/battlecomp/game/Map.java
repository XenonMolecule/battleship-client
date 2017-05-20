package com.xenonmolecule.battlecomp.game;

import com.xenonmolecule.battlecomp.game.ships.MapShip;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Map {

    public static final int MAP_WIDTH = 10, MAP_HEIGHT = 10;

    List<MapShip> ships = new ArrayList<>();
    double[][] coordinates;

    public Map() {
        this.coordinates = fillArr(MAP_WIDTH, MAP_HEIGHT, 1.0);
    }

    public void addShip(MapShip ship) throws MisplacedShipException {
        if(ships.size() < 5) {
            ship.place(this);
            ships.add(ship);
        }
    }

    private double[][] fillArr(int wid, int height, double val) {
        double[][] arr = new double[wid][height];
        for(int i = 0; i < wid; i ++) {
            for (int j = 0; j < height; j ++) {
                arr[i][j] = val;
            }
        }
        return arr;
    }

    public double[][] getCoords() {
        return coordinates;
    }

    public double getCoord(int x, int y) {
        if((x < 10 && x >= 0) && (y < 10 && y >= 0)) {
            return coordinates[y][x];
        } else {
            return 0.0;
        }
    }

    public boolean setCoord(int x, int y, boolean replace, double val) {
        if(((x < 10 && x >= 0) && (y < 10 && y >= 0)) && (replace || coordinates[y][x] != 0.0)) {
            coordinates[y][x] = val;
            return true;
        }
        return false;
    }

    public boolean setCoord(int x, int y, double val) {
        return setCoord(x,y,false,val);
    }

    public List<MapShip> getShips() {
        return ships;
    }

    public int[][] toBasicArr() {
        int[][] output = new int[MAP_WIDTH][MAP_HEIGHT];
        for(int i = 0; i < output.length; i ++) {
            for(int j = 0; j < output[i].length; j ++) {
                output[i][j] = (coordinates[i][j] == 0.0) ? 1 : 0;
            }
        }
        return output;
    }

    @Override
    public String toString() {
        NumberFormat fmt = new DecimalFormat("0.00");
        String output = "";
        for(int i = 0; i < coordinates.length; i ++) {
            for(int j = 0; j < coordinates[i].length; j ++) {
                output += fmt.format(coordinates[i][j]) + " ";
            }
            output+= (i < coordinates.length-1) ? "\n" : "";
        }
        return output;
    }
}
