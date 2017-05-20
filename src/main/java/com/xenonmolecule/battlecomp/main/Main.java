package com.xenonmolecule.battlecomp.main;

import com.xenonmolecule.battlecomp.bot.Botholomew.Botholomew;
import com.xenonmolecule.battlecomp.game.Map;
import com.xenonmolecule.battlecomp.game.ships.map.MapShip;

public class Main {
    public static void main(String[] args) {
        //new Battlecomp();
        Botholomew bot = new Botholomew();
        Map map = bot.autoPlaceShips();
        int[][] ships = map.toBasicArr();

        String print = "";
        for(int i = 0; i  < ships.length; i++) {
            for(int j = 0; j < ships[i].length; j ++) {
                print += ships[i][j] + " ";
            }
            print += "\n";
        }
        System.out.println(print);

        for(MapShip ship : map.getShips()) {
            System.out.println(ship);
        }
    }
}
