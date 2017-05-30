package com.xenonmolecule.battlecomp.bot.Botholomew.attacking;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.game.ships.hit.HitShip;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.math.Vector;
import com.xenonmolecule.battlecomp.math.WeightedRandom;

import java.util.ArrayList;

public class RandomAttacker {

    private HitMap map;
    private HitMap originalMap;
    private ArrayList<HitShip> validShips;
    private final double NEAR_MISS_COEFFICIENT = 0.25,
    NEAR_MISS_WEIGHT = 5.0,
    UNWEIGHTED_SUM_WEIGHT = 0.65,
    PARITY_FILTER_STRENGH = 0.05; // Lower means stronger
    private double[][] displayBoard = new double[Map.MAP_HEIGHT][Map.MAP_WIDTH];


    public RandomAttacker(HitMap map, ArrayList<HitShip> validShips) {
        this.map = map;
        this.originalMap = map.clone();
        this.validShips = validShips;
    }

    public Coordinate getAttackPosition() {
        // Stage 1: Generate position of all ships to figure out most likely orientations
        BoardScanner scanner = new BoardScanner(map);
        for(int i = 0; i < Map.MAP_HEIGHT; i ++) {
            for(int j = 0; j < Map.MAP_WIDTH; j ++) {
                scanner.weightBoard(new Coordinate(j,i), validShips);
            }
        }

        // Stage 2: Lay filter over board avoiding shooting next to shots and sunk ships
        ArrayList<Coordinate> misses = new ArrayList<Coordinate>();
        int[][] iMap = map.getMap();
        for(int y = 0; y < iMap.length; y++)
            for(int x = 0; x < iMap[y].length; x++)
                if(iMap[y][x] == -1)
                    misses.add(new Coordinate(x,y));

        double[][] wMap = new double[Map.MAP_HEIGHT][Map.MAP_WIDTH];

        for(int y = 0; y < wMap.length; y ++) {
            for(int x = 0; x < wMap[y].length; x ++) {
                wMap[y][x] = 1.0;
            }
        }

        for(Coordinate miss : misses) {
            try {
                int x = miss.getX();
                int y = miss.getY();

                if((x >= 0 && x < Map.MAP_WIDTH) && (y >= 0 && y < Map.MAP_HEIGHT)) {
                    wMap[miss.getY()][miss.getX()] = 0.0;
                    if(x-1 >= 0)
                        wMap[miss.getY()][miss.getX()-1] *= NEAR_MISS_COEFFICIENT;
                    if(x+1 < Map.MAP_WIDTH)
                        wMap[miss.getY()][miss.getX()+1] *= NEAR_MISS_COEFFICIENT;
                    if(y-1 >= 0)
                        wMap[miss.getY()-1][miss.getX()] *= NEAR_MISS_COEFFICIENT;
                    if(y+1 < Map.MAP_HEIGHT)
                        wMap[miss.getY()+1][miss.getX()] *= NEAR_MISS_COEFFICIENT;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("I'm bad at programming if this ever prints");
            }
        }

        // Stage 3: Generate weighted position of all ships to figure out more likely orientations
        HitMap nMap = originalMap.clone();
        for(int y = 0; y < Map.MAP_HEIGHT; y ++) {
            for(int x = 0; x < Map.MAP_WIDTH; x ++) {
                weightBoard(nMap, new Coordinate(x,y), validShips, wMap);
            }
        }
        int[][] wiMap = nMap.getMap();
        double[][] summedWeights = new double[Map.MAP_HEIGHT][Map.MAP_WIDTH];
        for(int i = 0; i < wiMap.length; i ++) {
            for(int j = 0; j < wiMap[i].length; j ++) {
                summedWeights[i][j] += (((wiMap[i][j]/10)*NEAR_MISS_WEIGHT) + (iMap[i][j]*UNWEIGHTED_SUM_WEIGHT));
            }
        }


        // Stage 4: Lay parity filter over board to prevent shots from being next to one another
        for(int i = 0; i < summedWeights.length; i ++) {
            for(int j = 0; j < summedWeights[i].length; j +=2) {
                if(i%2==0 && j==0)
                    j++;
                summedWeights[i][j] *= PARITY_FILTER_STRENGH;
            }
        }

        displayBoard = summedWeights;

        double [] oneDMap = new double[Map.MAP_WIDTH * Map.MAP_HEIGHT];
        for(int y = 0; y < summedWeights.length; y ++) {
            for(int x = 0; x < summedWeights[y].length; x ++) {
                oneDMap[(y * summedWeights.length) + x] = summedWeights[y][x];
            }
        }

        WeightedRandom gen = new WeightedRandom(oneDMap);
        return convNumToCoord(gen.generateRandom());
    }

    public void weightBoard(HitMap nMap, Coordinate point, ArrayList<HitShip> validShips, double[][] wMap) {
        int x = point.getX();
        int y = point.getY();
        int[][] mapArr = nMap.getMap();

        // Weight based on ships
        for(HitShip ship : validShips) {
            for(int i = 0; i < 4; i ++) {
                calcShip(ship, x, y, i, nMap, wMap);
            }
        }

        for(int i = 0; i < mapArr.length; i ++) {
            for(int j = 0; j < mapArr[i].length; j ++) {
                if(mapArr[i][j] == -1) {
                    try {
                        nMap.setPoint(j, i, true, 0);
                    } catch (MisplacedShipException e) {
                        System.out.println("This really should never ever be called");
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Something is broken in the hit detector");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void calcShip(HitShip ship, int x, int y, int orientation, HitMap nMap, double[][] wMap) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        ArrayList<Vector> shipVectors = ship.getVectors();
        for(int i = 0; i < shipVectors.size(); i ++) {
            vectors.add(shipVectors.get(i).rotate90(orientation));
        }
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for(Vector v : vectors) {
            coords.add(new Coordinate(x + v.getDX(), y + v.getDY()));
        }
        for(Vector v : vectors) {
            ArrayList<Coordinate> shiftedCoords = new ArrayList<Coordinate>();
            for (int i = 0; i < coords.size(); i++) {
                shiftedCoords.add(v.shiftCoord(coords.get(i)));
            }
            calcCoords(shiftedCoords, nMap, wMap);
        }

    }

    private void calcCoords(ArrayList<Coordinate> coords, HitMap nMap, double[][] wMap) {
        boolean cont = true;
        for(int i = 0; i < coords.size(); i ++) {
            cont = cont && nMap.testPoint(coords.get(i).getX(), coords.get(i).getY());
        }
        if(cont) {
            for(Coordinate coord : coords) {
                try {
                    nMap.addPoint(coord.getX(), coord.getY(), (int) (Math.round(10 * wMap[coord.getY()][coord.getX()])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Whelp, hitMap.testPoint really doesn't work...\n");
                    e.printStackTrace();
                } catch (MisplacedShipException e) {
                    System.out.println("Whelp, hitMap.testPoint doesn't work...\n");
                    e.printStackTrace();
                }
            }
        }
    }

    private Coordinate convNumToCoord(int num) {
        int y = num / Map.MAP_WIDTH;
        int x = num % Map.MAP_WIDTH;
        return new Coordinate(x,y);
    }

    double[][] getDisplayBoard() {
        return displayBoard;
    }

}
