package com.xenonmolecule.battlecomp.io;

import org.json.JSONException;
import org.json.JSONObject;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("x", x);
            object.put("y", y);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }

    public Coordinate addCoordinate(Coordinate coord) {
        return new Coordinate(x + coord.getX(), y + coord.getY());
    }
}
