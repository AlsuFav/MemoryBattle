CREATE TABLE cards (
                       id SERIAL PRIMARY KEY, -- Уникальный идентификатор карты
                       is_matched BOOLEAN DEFAULT FALSE, -- Найдена ли пара
                       is_revealed BOOLEAN DEFAULT FALSE, -- Открыта ли карта
                       unique_card_id INT, -- Уникальный ID, если тип карты NORMAL
                       type VARCHAR(50) NOT NULL, -- Тип карты (NORMAL, SPECIAL_CARD_EXTRA_TURN, ...)
                       image_path TEXT NOT NULL -- Путь до изображения карты
);

INSERT INTO cards (is_matched, is_revealed, unique_card_id, type, image_path)
VALUES
    (FALSE, FALSE, 1, 'NORMAL', '/images/card1.jpg'),
    (FALSE, FALSE, 1, 'NORMAL', '/images/card1.jpg'),
    (FALSE, FALSE, 2, 'NORMAL', '/images/card2.jpg'),
    (FALSE, FALSE, 2, 'NORMAL', '/images/card2.jpg'),
    (FALSE, FALSE, 3, 'NORMAL', '/images/card3.jpg'),
    (FALSE, FALSE, 3, 'NORMAL', '/images/card3.jpg'),
    (FALSE, FALSE, 4, 'NORMAL', '/images/card4.jpg'),
    (FALSE, FALSE, 4, 'NORMAL', '/images/card4.jpg'),
    (FALSE, FALSE, 5, 'NORMAL', '/images/card5.png'),
    (FALSE, FALSE, 5, 'NORMAL', '/images/card5.png'),
    (FALSE, FALSE, 6, 'NORMAL', '/images/card6.png'),
    (FALSE, FALSE, 6, 'NORMAL', '/images/card6.png'),
    (FALSE, FALSE, 7, 'NORMAL', '/images/card7.jpg'),
    (FALSE, FALSE, 7, 'NORMAL', '/images/card7.jpg'),
    (FALSE, FALSE, NULL, 'SPECIAL_CARD_EXTRA_TURN', '/images/special_extra_turn.png'),
    (FALSE, FALSE, NULL, 'SPECIAL_CARD_SHUFFLE', '/images/special_hint.png');