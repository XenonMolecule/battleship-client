package com.xenonmolecule.battlecomp.io;

// States are moved through in a procedural order
public enum GameState {
    DISCONNECTED, CONNECTED, JOINED, CAN_START, STARTED, BOARD_SETUP,
    BOARD_WAITING, CAN_PLAY, PLAYING, COMPLETED;
}
