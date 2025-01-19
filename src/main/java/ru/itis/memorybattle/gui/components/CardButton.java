package ru.itis.memorybattle.gui.components;

import javax.swing.*;
import java.awt.event.ActionListener;

public class CardButton extends JButton {
    private final int row;
    private final int col;

    public CardButton(int row, int col, ActionListener actionListener) {
        this.row = row;
        this.col = col;
        this.addActionListener(actionListener);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
