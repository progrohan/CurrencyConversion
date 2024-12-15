package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.ExchangeRatesDao;
import prog.rohan.currency_conversion.dto.ExchangeDTO;
import prog.rohan.currency_conversion.exceptions.NoExchangeException;
import prog.rohan.currency_conversion.model.ExchangeRatesModel;

import java.util.Optional;

public class ExchangeService {
    public static ExchangeDTO makeExchange(ExchangeDTO exchangeDTO){
        Optional<Double> rate = getStraightExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getReverseExchangeRate(exchangeDTO);
        if(rate.isEmpty()) rate = getCrossExchangeRate(exchangeDTO);
        if(rate.isEmpty()) throw new NoExchangeException("Не удалось конвертировать " +
                                                         exchangeDTO.getBaseCurrencyCode() + " в "+
                                                         exchangeDTO.getTargetCurrencyCode() + "!");
        exchangeDTO.setAmount(exchangeDTO.getAmount() * rate.get());
        return exchangeDTO;
    }

    public static Optional<Double> getStraightExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> exchangeRatesModel = ExchangeRatesDao.selectByCode(exchangeDTO.getBaseCurrencyCode(),
                exchangeDTO.getTargetCurrencyCode());
        if(exchangeRatesModel.isPresent()){
            rate = exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }

    public static Optional<Double> getReverseExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> exchangeRatesModel = ExchangeRatesDao.selectByCode(exchangeDTO.getTargetCurrencyCode(),
                exchangeDTO.getBaseCurrencyCode());
        if(exchangeRatesModel.isPresent()){
            rate = exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }
    public static Optional<Double> getCrossExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> usdToBase = ExchangeRatesDao.selectByCode("USD",
                exchangeDTO.getBaseCurrencyCode());
        Optional<ExchangeRatesModel> usdToTarget= ExchangeRatesDao.selectByCode("USD",
                exchangeDTO.getBaseCurrencyCode());
        if(usdToTarget.isEmpty() || usdToBase.isEmpty()) return Optional.empty();
        rate = usdToBase.get().getRate() / usdToTarget.get().getRate();
        return Optional.of(rate);
    }
}
