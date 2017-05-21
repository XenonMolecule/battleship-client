package com.xenonmolecule.battlecomp.bot.Botholomew.attacking;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;
import com.xenonmolecule.battlecomp.game.ships.hit.HitShip;
import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.math.Vector;
import com.xenonmolecule.battlecomp.math.WeightedRandom;

import java.util.ArrayList;
import java.util.Random;

public class BoardScanner {

    HitMap hitMap;

    public BoardScanner(HitMap hitMap) {
        this.hitMap = hitMap;
    }

    public void weightBoard(Coordinate point, ArrayList<HitShip> validShips) {
        int x = point.getX();
        int y = point.getY();
        int[][] map = hitMap.getMap();

        // Weight based on ships
        for(HitShip ship : validShips) {
            for(int i = 0; i < 4; i ++) {
                calcShip(ship, x, y, i);
            }
        }

        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[i].length; j ++) {
                if(map[i][j] == 1) {
                    try {
                        hitMap.setPoint(j, i, true, 0);
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

    private void calcShip(HitShip ship, int x, int y, int orientation) {
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
            calcCoords(shiftedCoords);
        }

    }

    private void calcCoords(ArrayList<Coordinate> coords) {
        boolean cont = true;
        for(int i = 0; i < coords.size(); i ++) {
            cont = cont && hitMap.testPoint(coords.get(i).getX(), coords.get(i).getY());
        }
        if(cont) {
            for(Coordinate coord : coords) {
                try {
                    hitMap.addPoint(coord.getX(), coord.getY(), 1);
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

    public Coordinate getBestShot(ArrayList<HitShip> unsunk) {
        int[][] map = hitMap.getMap();

        boolean noHits = true;
        ArrayList<Coordinate> hits = new ArrayList<Coordinate>();
        ArrayList<Coordinate> misses = new ArrayList<Coordinate>();
        for(int y = 0; y < map.length; y ++) {
            for(int x = 0; x < map[y].length; x ++) {
                noHits = noHits && map[y][x] != 1;
                if(map[y][x] == 1)
                    hits.add(new Coordinate(x,y));
                else if(map[y][x] == -1)
                    misses.add(new Coordinate(x,y));
            }
        }
        if(!noHits) {
            System.out.println("Picking shot pos based on hit");
            for(Coordinate hit : hits) {
                weightBoard(hit, unsunk);
            }

            // Make sure we don't shoot at hits and misses
            for(Coordinate hit : hits) {
                try {
                    hitMap.setPoint(hit.getX(), hit.getY(), true, 0);
                } catch (Exception e) {
                    System.out.println("I would be surprised if I EVER saw this get printed out");
                }
            }

            for(Coordinate miss : misses) {
                try {
                    hitMap.setPoint(miss.getX(), miss.getY(), true, 0);
                } catch (Exception e) {
                    System.out.println("I would be surprised if I EVER saw this get printed out");
                }
            }

            int[][] wMap = hitMap.getMap();
            int max = -1;
            ArrayList<Coordinate> bestShots = new ArrayList<Coordinate>();
            for(int y = 0; y < wMap.length; y ++) {
                for(int x = 0; x < wMap[y].length; x ++) {
                    if(map[y][x] > max) {
                        bestShots.clear();
                        max = map[y][x];
                        bestShots.add(new Coordinate(x,y));
                    } else if(map[y][x] == max) {
                        bestShots.add(new Coordinate(x,y));
                    }
                }
            }

            String print = "";
            for(int i = 0; i < wMap.length; i++) {
                for(int j = 0; j < wMap[i].length; j ++) {
                    print += wMap[i][j] + " ";
                }
                print += "\n";
            }
            System.out.println(print);

            // Fire at one of the most likely spots
            if(bestShots.size() > 1) {
                Random gen = new Random();
                return bestShots.get(gen.nextInt(bestShots.size()));
            } else {
                return bestShots.get(0);
            }
        } else {
            System.out.println("Taking weighted random shot");
            // Take weighted random shot
            for(int y = 0; y < map.length; y ++) {
                for(int x = 0; x < map.length; x ++) {
                    if(map[y][x] != -1){
                        try {
                            hitMap.setPoint(x,y,false,10);
                        } catch (Exception e) {}
                    }
                }
            }

            for(int y = 0; y < map.length; y ++) {
                for(int x = 0; x < map.length; x ++) {
                    if(map[y][x] == -1){
                        try {
                            hitMap.setPoint(x + 1, y, true, 1);
                        } catch (Exception e) {}
                        try {
                            hitMap.setPoint(x - 1, y, true, 1);
                        } catch (Exception e) {}
                        try {
                            hitMap.setPoint(x, y + 1, true, 1);
                        } catch (Exception e) {}
                        try {
                            hitMap.setPoint(x, y - 1, true, 1);
                        } catch (Exception e) {}
                        try {
                            hitMap.setPoint(x, y, true, 0);
                        } catch (Exception e) {}
                    }
                }
            }

            int[][] wMap = hitMap.getMap();

            // Prevent wasted shots on places where hitting is impossible
            for(int y = 0; y < wMap.length; y ++) {
                for(int x = 0; x < wMap[y].length; x ++) {
                    if(wMap[y][x] == 1) {
                        int sum = 0;
                        try {
                            if(hitMap.getPoint(x,y-1) == -1)
                                sum++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sum++;
                        }
                        try {
                            if(hitMap.getPoint(x,y+1) == -1)
                                sum++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sum++;
                        }
                        try {
                            if(hitMap.getPoint(x+1,y) == -1)
                                sum++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sum++;
                        }
                        try {
                            if(hitMap.getPoint(x-1,y) == -1)
                                sum++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            sum++;
                        }
                        if(sum==4) {
                            try {
                                hitMap.setPoint(x, y, true, 0);
                            } catch (Exception e) {}
                        }
                    }
                }
            }

            wMap = hitMap.getMap();

            int [] oneDMap = new int[Map.MAP_WIDTH * Map.MAP_HEIGHT];
            for(int y = 0; y < wMap.length; y ++) {
                for(int x = 0; x < wMap[y].length; x ++) {
                    oneDMap[(y * wMap.length) + x] = wMap[y][x];
                }
            }

            WeightedRandom gen = new WeightedRandom(oneDMap);
            return convNumToCoord(gen.generateRandom());
        }
    }

    private Coordinate convNumToCoord(int num) {
        int y = num / Map.MAP_WIDTH;
        int x = num % Map.MAP_WIDTH;
        return new Coordinate(x,y);
    }

}
