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

    public void open(String source) {
        isFlipped = !isFlipped;
        ImageIcon icon = new ImageIcon(
                new ImageIcon(getClass().getResource(source)) // Загружаем из ресурсов
                        .getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH) // Масштабируем изображение
        );
        setIcon(icon);
        setBackground(Color.WHITE);

    }

    public void close() {
        isFlipped = !isFlipped;
        setIcon(null);
        setBackground(Color.GRAY);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }
}

