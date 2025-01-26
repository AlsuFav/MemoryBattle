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

        // Добавляем рамку вокруг кнопки
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void open(String source) {
        if (isFlipped || isMatched) return; // Если уже перевернута или совпала, ничего не делаем

        animateCard(() -> {
            // Логика обновления состояния кнопки и отображения картинки
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

            if (isMatched) {
                matchedIcon = icon;
            }
        }, Color.WHITE);
    }

    public void close() {
        if (!isFlipped || isMatched) return; // Если уже закрыта или совпала, ничего не делаем

        animateCard(() -> {
            // Логика обновления состояния кнопки при закрытии
            isFlipped = !isFlipped;
            setIcon(null);
            setBackground(Color.GRAY);
        }, Color.GRAY);
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

    /**
     * Метод для анимации карточки сужения и расширения.
     *
     * @param updateState Логика обновления состояния кнопки (лямбда-выражение).
     * @param fillColor   Цвет, который будет заполнять карточку во время анимации.
     */
    private void animateCard(Runnable updateState, Color fillColor) {
        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            int width = getWidth();
            int height = getHeight();
            Graphics g = getGraphics();

            // Сужаем карточку до 0 по ширине
            for (int i = width; i >= 0; i -= 8) {
                g.clearRect(0, 0, width, height);
                g.setColor(fillColor);
                g.fillRect((width - i) / 2, 0, i, height);
                try {
                    Thread.sleep(10); // Пауза для плавности
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            // Выполняем обновление состояния кнопки
            updateState.run();

            // Расширяем карточку обратно до нормального размера
            for (int i = 0; i <= width; i += 8) {
                g.clearRect(0, 0, width, height);
                g.setColor(fillColor);
                g.fillRect((width - i) / 2, 0, i, height);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            timer.stop();
        });
        timer.start();
    }
}