package com.xenonmolecule.battlecomp.game.ships.hit;

import com.xenonmolecule.battlecomp.io.Coordinate;
import com.xenonmolecule.battlecomp.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum HitShip {

    PATROL ("Patrol", new ArrayList<Vector>(
            Arrays.asList(
                    new Vector(0,0),
                    new Vector(1,0)
            )
    )),
    CRUISER ("Cruiser", new ArrayList<Vector>(
            Arrays.asList(
                    new Vector(0,0),
                    new Vector(1,0),
                    new Vector(2,0)
            )
    )),
    BATTLESHIP ("Battleship", new ArrayList<Vector>(
            Arrays.asList(
                    new Vector(0,0),
                    new Vector(1,0),
                    new Vector(2,0),
                    new Vector(3,0)
            )
    )),
    SUBMARINE ("Submarine", new ArrayList<Vector>(
            Arrays.asList(
                    new Vector(0,0),
                    new Vector(1,0),
                    new Vector(0,1),
                    new Vector(-1,0)
            )
    )),
    AIRCRAFT_CARRIER ("Aircraft Carrier", new ArrayList<Vector>(
            Arrays.asList(
                    new Vector(0,0),
                    new Vector(1,0),
                    new Vector(1,1),
                    new Vector(2,0),
                    new Vector(2,1),
                    new Vector(3,1)
            )
    ));

    private String name;
    private ArrayList<Vector> vectors;

    HitShip(String name, ArrayList<Vector> vectors) {
        this.name = name;
        this.vectors = vectors;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Vector> getVectors() {
        return vectors;
    }

}
