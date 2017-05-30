package com.xenonmolecule.battlecomp.graphics;



import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;

public class BattleshipBoard extends JPanel {
    private static ArrayList<BattleshipBoard> boards = new ArrayList<>();
    protected GridBagConstraints gbc;
    protected final BattleshipCell[][] cells = new BattleshipCell[10][10];

    /*
        // Our basic board showing hits misses and kills
        JPanel basicBoardCont = new JPanel(new BorderLayout());
        JLabel basicBoardLabel = new JLabel("Basic Board:");
        basicBoardOurs = new BattleshipBoard();
        basicBoardOurs.setCells(map);
        basicBoardCont.add(basicBoardLabel, BorderLayout.NORTH);
        basicBoardCont.add(basicBoardOurs, BorderLayout.CENTER);
        frame.getContentPane().add(basicBoardCont);
     */

    public BattleshipBoard() {
        boards.add(this);
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
        //gbc.gridwidth = GridBagConstraints.REMAINDER;
        //gbc.weightx = 1;
        //gbc.weighty = 1;
        populateCells();
    }

    protected void populateCells() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                BattleshipCell cell = new BattleshipCell(this, i, j);
                cell.setValue(0.0f);
                gbc.gridx = j;
                gbc.gridy = i;
                cells[i][j] = cell;
                add(cell, gbc);
            }
        }
    }

    public void setCells(float[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                cells[row][col].setValue(map[row][col]);
            }
        }
        repaint();
    }

    public void setCells(int[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                cells[row][col].setValueInt(map[row][col]);
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {

    }
}