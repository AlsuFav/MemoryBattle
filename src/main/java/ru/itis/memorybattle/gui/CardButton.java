package ru.itis.memorybattle.gui;

import javax.swing.*;
import java.awt.*;

public class CardButton extends JButton {
    private final int row;
    private final int col;
    private boolean isFlipped;
    private boolean isMatched;
    private ImageIcon matchedIcon;

    public CardButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.isFlipped = false;
        this.isMatched = false;

        setPreferredSize(new Dimension(80, 80));
        setBackground(Color.GRAY);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
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
                new ImageIcon(getClass().getResource(source))
                        .getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH)
        );

        setBackground(Color.WHITE);
        setIcon(icon);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // Если это совпавшая пара, сохраняем иконку
        if (isMatched) {
            matchedIcon = icon;
        }

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

        // Если кнопка совпала, сохранить текущую иконку
        if (matched) {
            matchedIcon = (ImageIcon) getIcon();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isMatched) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            if (matchedIcon != null) {
                int iconX = (getWidth() - matchedIcon.getIconWidth()) / 2;
                int iconY = (getHeight() - matchedIcon.getIconHeight()) / 2;
                matchedIcon.paintIcon(this, g, iconX, iconY);
            }
        } else {
            super.paintComponent(g);
        }
    }
}
