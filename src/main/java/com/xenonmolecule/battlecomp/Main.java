package com.xenonmolecule.battlecomp;

import com.xenonmolecule.battlecomp.bot.Bot;
import com.xenonmolecule.battlecomp.bot.Botholomew.Botholomew;
import com.xenonmolecule.battlecomp.io.GameState;
import com.xenonmolecule.battlecomp.main.Battlecomp;

import java.util.Scanner;

public class Main {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        /*System.out.print("Joining or hosting? [j/h]: ");
        String response = scan.nextLine();
        System.out.println();
        if (response.equals("j")) {
            Battlecomp comp = new Battlecomp();
            System.out.print("Enter game code: ");
            comp.joinGame(scan.nextInt());
        } else if (response.equals("h")) {
            Battlecomp comp = new Battlecomp();
            comp.createGame();
        }*/
        /*
        while(true) {
            Bot comp = new Botholomew();
            comp.joinGame(1);
            while(comp.getState()!= GameState.COMPLETED) {

            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }
}