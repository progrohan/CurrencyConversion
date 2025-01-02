INSERT INTO Currencies (Code, FullName, Sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('CZK', 'Czech Koruna', 'Kč'),
       ('GBP', 'Pound Sterling', '£'),
       ('CNY', 'Yuan Renminbi', '¥'),
       ('JPY', 'Yen', '¥'),
       ('SEK', 'Swedish Krona', 'kr'),
       ('TRY', 'Turkish Lira', '₺');

INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (1, 2, 0.92),
       (1, 3, 22.94),
       (1, 4, 0.79),
       (1, 5, 7.18),
       (1, 6, 148.04),
       (3, 8, 0.21),
       (4, 5, 9.11),
       (6, 2, 0.0062),
       (7, 2, 0.088);