package com.xenonmolecule.battlecomp.io;

import org.json.JSONException;
import org.json.JSONObject;

public class PlacedShip {
    private String shipType;
    private int length = -1;
    private int x;
    private int y;
    private int orientation;

    public PlacedShip(String shipType, int x, int y, int orientation) {
        this.shipType = shipType;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public PlacedShip(String shipType, int length, int x, int y, int orientation) {
        this(shipType, x, y, orientation);
        this.length = length;
    }

    public String getShipType() {
        return shipType;
    }

    public int getLength() {
        return length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrientation() {
        return orientation;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type",shipType);
            json.put("x",x);
            json.put("y",y);
            json.put("orientation",orientation);
            if (length > -1)
                json.put("length",length);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
