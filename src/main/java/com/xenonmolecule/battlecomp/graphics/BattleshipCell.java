package com.xenonmolecule.battlecomp.graphics;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

public class BattleshipCell extends JPanel {
    private static DecimalFormat format = new DecimalFormat(".00");
    private JLabel valueLabel;
    private float value;
    private StatusColor status;
    private int row;
    private int col;

    public BattleshipCell(BattleshipBoard board, int row, int col) {
        valueLabel = new JLabel();
        add(valueLabel);
        this.row = row;
        this.col = col;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ignored
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Ignored
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Ignored
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Ignored
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Ignored
            }
        });
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public enum StatusColor {
        IMPOSSIBLE, POSSIBLE, NEUTRAL, HIT, MISS, KILL;
    }

    public void setValue(float value) {
        this.value = value;
        if (value == 0f) {
            valueLabel.setVisible(false);
            status = StatusColor.NEUTRAL;
        } else if (value < 0) {
            valueLabel.setVisible(false);
            status = StatusColor.IMPOSSIBLE;
        } else if ( value <= 1){
            valueLabel.setText("<html><font color='white'>" + format.format(value) + "</font></html>");
            valueLabel.setVisible(true);
            status = StatusColor.POSSIBLE;
        } else {
            valueLabel.setVisible(false);
        }
        setBackground();
    }

    public void setValueInt(int value) {
        this.value = value;
        valueLabel.setVisible(false);
        if (value == 0) {
            status = StatusColor.NEUTRAL;
        }
        if (value == 1) {
            status = StatusColor.HIT;
        }
        if (value == 2) {
            status = StatusColor.MISS;
        }
        if (value == 3) {
            status = StatusColor.KILL;
        }
        setBackground();
    }

    private void setBackground() {
        float h = 1.0f;
        float s = 0.0f;
        if (!(status == StatusColor.NEUTRAL)) {
            if (status == StatusColor.POSSIBLE) {
                h = 0.25f;
                s = 1.0f;
            } else if (status == StatusColor.IMPOSSIBLE || status == StatusColor.HIT) {
                s = 1.0f;
            } else if (status == StatusColor.MISS) {
                setBackground(Color.darkGray);
                return;
            }
            else if (status == StatusColor.KILL) {
                setBackground(Color.YELLOW);
                return;
            }
            setBackground(Color.getHSBColor(h, s, value >= 0 ? value : 1));
        } else {
            setBackground(Color.lightGray);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(25, 25);
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
