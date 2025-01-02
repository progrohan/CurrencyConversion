package prog.rohan.currency_conversion.dao;

import prog.rohan.currency_conversion.model.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDAO extends BaseDAO<ExchangeRate, Long> {

    Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode);

}
