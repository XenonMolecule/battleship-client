package com.xenonmolecule.battlecomp.bot.Botholomew;

import com.xenonmolecule.battlecomp.bot.Bot;
import com.xenonmolecule.battlecomp.game.Map;

public class Botholomew extends Bot {

    @Override
    public void takeTurn() {

    }

    @Override
    public Map placeShips() {
        Placer botholomewPlacer = new Placer();
        return botholomewPlacer.generateBoard();
    }
}
