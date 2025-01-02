package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.model.Currency;

import java.util.Optional;

public interface CurrencyDAO extends BaseDAO<Currency, Integer>{

    Optional<Currency> findByCode(String code);

}
