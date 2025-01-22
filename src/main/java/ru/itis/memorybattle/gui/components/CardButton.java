package ru.itis.memorybattle.gui.components;

import javax.swing.*;
import java.awt.*;

public class CardButton extends JButton {
    private final int row;
    private final int col;
    private final int cardId; // ID карточки из базы данных
    private final String imageUrl; // Ссылка на изображение карточки
    private boolean isFlipped;
    private boolean isMatched;
    private final ImageIcon cardImage; // Иконка для изображения карты


    public CardButton(int row, int col, int cardId, String imageUrl) {
        this.row = row;
        this.col = col;
        this.cardId = cardId;
        this.imageUrl = imageUrl;
        this.isFlipped = false;
        this.isMatched = false;

        this.cardImage = new ImageIcon(
                new ImageIcon(getClass().getResource(imageUrl)) // Загружаем из ресурсов
                        .getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH) // Масштабируем изображение
        );

        // Устанавливаем размеры кнопки и начальный фон
        setPreferredSize(new Dimension(80, 80));
        setBackground(Color.GRAY);
        setFocusPainted(false); // Убираем обводку фокуса
        setContentAreaFilled(false); // Отключаем заливку кнопки
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCardId() {
        return cardId;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flip() {
        isFlipped = !isFlipped;
        repaint(); // Перерисовываем кнопку, чтобы отобразить изменения
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
        if (matched) {
            setEnabled(false); // Отключаем кнопку
        }
        repaint(); // Перерисовываем кнопку
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Устанавливаем цвет фона
        if (isFlipped || isMatched) {
            g.setColor(Color.WHITE); // Белый фон для перевёрнутой карты
        } else {
            g.setColor(Color.GRAY); // Серый фон для закрытой карты
        }
        g.fillRect(0, 0, getWidth(), getHeight()); // Заливаем фон кнопки

        // Если карта перевёрнута или совпала, рисуем изображение
        if (isFlipped || isMatched) {
            int buttonWidth = getWidth();
            int buttonHeight = getHeight();

            // Определяем размер квадратной области для изображения
            int squareSize = Math.min(buttonWidth, buttonHeight) - 20; // Размер квадрата с отступом
            int x = (buttonWidth - squareSize) / 2; // Центрируем по горизонтали
            int y = (buttonHeight - squareSize) / 2; // Центрируем по вертикали

            // Рисуем изображение в квадратной области
            g.drawImage(cardImage.getImage(), x, y, squareSize, squareSize, this);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
    }
}
