package com.xenonmolecule.battlecomp.bot.Botholomew.attacking;

import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.MisplacedShipException;

public class HitMap implements Cloneable {

    private int[][] map;

    public HitMap(int[][] map) {
        this.map = map;
    }

    public int[][] getMap() {
        return map;
    }

    public int getPoint(int x, int y) throws ArrayIndexOutOfBoundsException {
        if((x < Map.MAP_WIDTH && x >= 0) && (y < Map.MAP_HEIGHT && y >= 0))
            return map[y][x];
        else
            throw new ArrayIndexOutOfBoundsException();
    }

    public void setPoint(int x, int y, boolean replace, int val) throws ArrayIndexOutOfBoundsException, MisplacedShipException{
        if((x < Map.MAP_WIDTH && x >= 0) && (y < Map.MAP_HEIGHT && y >= 0)) {
            if(replace || map[y][x] == 0) {
                map[y][x] = val;
            } else {
                throw new MisplacedShipException(x,y,"Something already here");
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public void setPoint(int x, int y, int val) throws ArrayIndexOutOfBoundsException, MisplacedShipException {
        setPoint(x , y, false, val);
    }

    public void addPoint(int x, int y, boolean repNeg, int val) throws ArrayIndexOutOfBoundsException, MisplacedShipException {
        if((x < Map.MAP_WIDTH && x >= 0) && (y < Map.MAP_HEIGHT && y >= 0)) {
            if(repNeg || map[y][x] >= 0) {
                setPoint(x, y, true, getPoint(x, y) + val);
            } else {
                throw new MisplacedShipException(x,y, "This point is already sunk or missed");
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }

    }

    public void addPoint(int x, int y, int val) throws ArrayIndexOutOfBoundsException, MisplacedShipException {
        addPoint(x, y, false, val);
    }

    public boolean testPoint(int x, int y) {
        return (((x < Map.MAP_WIDTH && x >= 0) && (y < Map.MAP_HEIGHT && y >= 0)) && map[y][x] >= 0);
    }

    @Override
    public HitMap clone() {
        int[][] points = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[i].length; j ++) {
                points[i][j] = map[i][j];
            }
        }
        return new HitMap(points);
    }


}
