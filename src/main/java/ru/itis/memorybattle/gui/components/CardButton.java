package ru.itis.memorybattle.gui.components;

import javax.swing.*;
import java.awt.*;

public class CardButton extends JButton {
    private final int row;
    private final int col;
    private boolean isFlipped;
    private boolean isMatched;

    public CardButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.isFlipped = false;
        this.isMatched = false;
        setPreferredSize(new Dimension(80, 80));
        setBackground(Color.GRAY);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flip() {
        isFlipped = !isFlipped;
        setText(isFlipped ? "Card" : "");
        setBackground(isFlipped ? Color.WHITE : Color.GRAY);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
        if (matched) {
            setBackground(Color.GREEN); // Зеленый для совпавших
        }
    }
}
