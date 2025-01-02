CREATE TABLE IF NOT EXISTS Currencies
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    Code      TEXT CHECK ( length(code) == 3 ) NOT NULL,
    FullName TEXT                             NOT NULL,
    Sign      TEXT                             NOT NULL,
    CONSTRAINT currencies_unique_code UNIQUE (Code)
    );

CREATE TABLE IF NOT EXISTS ExchangeRates
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId   INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate               REAL    NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (id),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (id),
    CONSTRAINT exchange_rates_unique_currency_ids UNIQUE (BaseCurrencyId, TargetCurrencyId)
    );