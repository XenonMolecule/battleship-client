package com.xenonmolecule.battlecomp.bot.Botholomew;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.game.ships.map.AircraftCarrierShip;
import com.xenonmolecule.battlecomp.game.ships.map.MapShip;
import com.xenonmolecule.battlecomp.game.ships.map.StandardShip;
import com.xenonmolecule.battlecomp.game.ships.map.SubmarineShip;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.math.WeightedRandom;

import java.util.ArrayList;
import java.util.Random;

public class Placer {

    Map map;

    public Placer(int mapW, int mapH) {
        this.map = new Map();
        if(mapW != 10 && mapH != 10)
            System.out.println("Yeeeaaaah, you are going to have to change the Map class for a board size other than 10... sorry");
    }

    public Placer() {
        this.map = new Map();
    }

    public Map generateBoard() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        for(int i = 0; i < 5; i ++) {
            order.add(i);
        }
        Random gen = new Random();
        for(int i = order.size()-1; i > 0; i --) {
            int index = gen.nextInt(i+1);
            order = swapInt(i,index,order);
        }
        System.out.println(order);
        for(int i = 0; i < order.size(); i++) {
            System.out.println("-------------------------------");
            placeShip(order.get(i));
            System.out.println("-------------------------------");
        }
        return map;
    }

    public Coordinate convNumToCoord(int num) {
        int y = num / Map.MAP_WIDTH;
        int x = num % Map.MAP_WIDTH;
        return new Coordinate(x,y);
    }

    private Coordinate genRandPosFromBoard() {
        double[] weights = new double[Map.MAP_WIDTH * Map.MAP_HEIGHT];
        double[][] coordinates = map.getCoords();
        for(int i = 0; i < coordinates.length; i ++) {
            for(int j = 0; j < coordinates[i].length; j ++) {
                weights[(i * coordinates.length) + j] = coordinates[i][j];
            }
        }
        WeightedRandom gen = new WeightedRandom(weights);
        return convNumToCoord(gen.generateRandom());
    }

    private void placeShip(int num) {
        Coordinate coord = genRandPosFromBoard();
        System.out.println(num + " (" + coord.getX() + "," + coord.getY() + ")");
        Random gen = new Random();
        MapShip ship;
        switch(num) {
            case 0:
                ship = new StandardShip("Patrol",2,coord.getX(),coord.getY(),gen.nextInt(4));
                break;
            case 1:
                ship = new StandardShip("Cruiser",3,coord.getX(),coord.getY(),gen.nextInt(4));
                break;
            case 2:
                ship = new StandardShip("Battleship",4,coord.getX(),coord.getY(),gen.nextInt(4));
                break;
            case 3:
                ship = new SubmarineShip(coord.getX(), coord.getY(), gen.nextInt(4));
                break;
            case 4:
                ship = new AircraftCarrierShip(coord.getX(), coord.getY(), gen.nextInt(4));
                break;
            default:
                ship = new StandardShip("Patrol", 2, coord.getX(), coord.getY(), gen.nextInt(4));
        }
        try {
            map.addShip(ship);
        } catch (MisplacedShipException e) {
            placeShip(num);
        }
    }

    private ArrayList<Integer> swapInt (int index1, int index2, ArrayList<Integer> arr) {
        int temp = arr.get(index1);
        arr.set(index1, arr.get(index2));
        arr.set(index2, temp);
        return arr;
    }

}
