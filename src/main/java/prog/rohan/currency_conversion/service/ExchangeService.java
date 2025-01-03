package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.JdbcCurrencyDAO;
import prog.rohan.currency_conversion.dao.JdbcExchangeRateDAO;
import prog.rohan.currency_conversion.dto.CurrencyResponseDTO;
import prog.rohan.currency_conversion.dto.ExchangeRequestDTO;
import prog.rohan.currency_conversion.dto.ExchangeResponseDTO;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.exceptions.NoExchangeException;
import prog.rohan.currency_conversion.model.Currency;
import prog.rohan.currency_conversion.model.ExchangeRate;

import java.util.Optional;

public class ExchangeService {
    private static JdbcExchangeRateDAO jdbcExchangeRateDAO = JdbcExchangeRateDAO.getINSTANCE();
    private static JdbcCurrencyDAO jdbcCurrencyDAO = JdbcCurrencyDAO.getINSTANCE();

    public static ExchangeResponseDTO makeExchange(ExchangeRequestDTO exchangeDTO){
        Optional<Double> rate = getStraightExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getReverseExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getCrossExchangeRate(exchangeDTO);
        if(rate.isEmpty()) throw new NoExchangeException("Не удалось конвертировать " +
                                                         exchangeDTO.getBaseCurrencyCode() + " в "+
                                                         exchangeDTO.getTargetCurrencyCode() + "!");

        String baseCurrencyCode = exchangeDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeDTO.getTargetCurrencyCode();
        Currency baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode).
                orElseThrow(() -> new DataNotFoundException("Currency with code " +
                                                            baseCurrencyCode +
                                                            " not found!"));
        Currency targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode).
                orElseThrow(() -> new DataNotFoundException("Currency with code " +
                                                            targetCurrencyCode +
                                                            " not found!"));
        return new ExchangeResponseDTO(
                new CurrencyResponseDTO(
                        baseCurrency.getId(),
                        baseCurrency.getCode(),
                        baseCurrency.getFullName(),
                        baseCurrency.getSign()
                ),
                new CurrencyResponseDTO(
                        targetCurrency.getId(),
                        targetCurrency.getCode(),
                        targetCurrency.getFullName(),
                        targetCurrency.getSign()
                ),
                rate.get(),
                exchangeDTO.getAmount(),
                exchangeDTO.getAmount() * rate.get()
        );
    }

    public static Optional<Double> getStraightExchangeRate(ExchangeRequestDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> exchangeRatesModel = jdbcExchangeRateDAO.findByCodes(
                        exchangeDTO.getBaseCurrencyCode(),
                        exchangeDTO.getTargetCurrencyCode());
        if(exchangeRatesModel.isPresent()){
            rate = exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }

    public static Optional<Double> getReverseExchangeRate(ExchangeRequestDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> exchangeRatesModel = jdbcExchangeRateDAO.findByCodes(
                        exchangeDTO.getTargetCurrencyCode(),
                        exchangeDTO.getBaseCurrencyCode());
        if(exchangeRatesModel.isPresent()){
            rate = 1 / exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }
    public static Optional<Double> getCrossExchangeRate(ExchangeRequestDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> usdToBase = jdbcExchangeRateDAO.findByCodes(
                        "USD",
                        exchangeDTO.getBaseCurrencyCode()
                        );
        Optional<ExchangeRate> usdToTarget= jdbcExchangeRateDAO.findByCodes(
                        "USD",
                        exchangeDTO.getTargetCurrencyCode());
        if(usdToTarget.isEmpty() || usdToBase.isEmpty()) return Optional.empty();
        rate = usdToTarget.get().getRate() / usdToBase.get().getRate();
        return Optional.of(rate);
    }
}
