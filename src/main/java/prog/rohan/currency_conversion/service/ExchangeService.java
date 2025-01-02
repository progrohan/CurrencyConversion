package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.JdbcExchangeRateDAO;
import prog.rohan.currency_conversion.dto.ExchangeDTO;
import prog.rohan.currency_conversion.exceptions.NoExchangeException;
import prog.rohan.currency_conversion.model.ExchangeRate;

import java.util.Optional;

public class ExchangeService {
    private static JdbcExchangeRateDAO jdbcExchangeRateDAO = JdbcExchangeRateDAO.getINSTANCE();

    public static ExchangeDTO makeExchange(ExchangeDTO exchangeDTO){
        Optional<Double> rate = getStraightExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getReverseExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getCrossExchangeRate(exchangeDTO);
        if(rate.isEmpty()) throw new NoExchangeException("Не удалось конвертировать " +
                                                         exchangeDTO.getBaseCurrencyDTO().getCode() + " в "+
                                                         exchangeDTO.getTargetCurrencyDTO().getCode() + "!");
        exchangeDTO.setRate(rate.get());
        exchangeDTO.setConvertedAmount(exchangeDTO.getAmount() * rate.get());
        return exchangeDTO;
    }

    public static Optional<Double> getStraightExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> exchangeRatesModel = jdbcExchangeRateDAO.findByCodes(
                        exchangeDTO.getBaseCurrencyDTO().getCode(),
                        exchangeDTO.getTargetCurrencyDTO().getCode());
        if(exchangeRatesModel.isPresent()){
            rate = exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }

    public static Optional<Double> getReverseExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> exchangeRatesModel = jdbcExchangeRateDAO.findByCodes(
                        exchangeDTO.getTargetCurrencyDTO().getCode(),
                        exchangeDTO.getBaseCurrencyDTO().getCode());
        if(exchangeRatesModel.isPresent()){
            rate = 1 / exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }
    public static Optional<Double> getCrossExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRate> usdToBase = jdbcExchangeRateDAO.findByCodes(
                        "USD",
                        exchangeDTO.getBaseCurrencyDTO().getCode()
                        );
        Optional<ExchangeRate> usdToTarget= jdbcExchangeRateDAO.findByCodes(
                        "USD",
                        exchangeDTO.getTargetCurrencyDTO().getCode());
        if(usdToTarget.isEmpty() || usdToBase.isEmpty()) return Optional.empty();
        rate = usdToTarget.get().getRate() / usdToBase.get().getRate();
        return Optional.of(rate);
    }
}
