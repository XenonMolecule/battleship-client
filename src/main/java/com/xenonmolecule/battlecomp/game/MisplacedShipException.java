package com.xenonmolecule.battlecomp.game;

public class MisplacedShipException extends Exception {

    public MisplacedShipException(int x, int y, String reason) {
        super("Tried to place ship at invalid position (" + x + "," + y + ").  This position is invisible because of reason: " + reason);
    }

}
