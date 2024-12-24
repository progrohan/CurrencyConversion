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
                                                         exchangeDTO.getBaseCurrencyDTO().getCode() + " в "+
                                                         exchangeDTO.getTargetCurrencyDTO().getCode() + "!");
        exchangeDTO.setRate(rate.get());
        exchangeDTO.setConvertedAmount(exchangeDTO.getAmount() * rate.get());
        return exchangeDTO;
    }

    public static Optional<Double> getStraightExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> exchangeRatesModel = ExchangeRatesDao.selectByCode
                (new ExchangeRatesModel(null,
                        exchangeDTO.getBaseCurrencyDTO().getCode(),
                        exchangeDTO.getTargetCurrencyDTO().getCode(),
                        null));
        if(exchangeRatesModel.isPresent()){
            rate = exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }

    public static Optional<Double> getReverseExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> exchangeRatesModel = ExchangeRatesDao.selectByCode
                (new ExchangeRatesModel(null,
                        exchangeDTO.getTargetCurrencyDTO().getCode(),
                        exchangeDTO.getBaseCurrencyDTO().getCode(),
                        null));
        if(exchangeRatesModel.isPresent()){
            rate = 1 / exchangeRatesModel.get().getRate();
        }else rate = null;
        return Optional.ofNullable(rate);
    }
    public static Optional<Double> getCrossExchangeRate(ExchangeDTO exchangeDTO){
        Double rate;
        Optional<ExchangeRatesModel> usdToBase = ExchangeRatesDao.selectByCode
                ((new ExchangeRatesModel(null,
                        "USD",
                        exchangeDTO.getBaseCurrencyDTO().getCode(),
                        null)));
        Optional<ExchangeRatesModel> usdToTarget= ExchangeRatesDao.selectByCode
                (new ExchangeRatesModel(null,
                        "USD",
                        exchangeDTO.getTargetCurrencyDTO().getCode(),
                        null));
        if(usdToTarget.isEmpty() || usdToBase.isEmpty()) return Optional.empty();
        rate = usdToTarget.get().getRate() / usdToBase.get().getRate();
        return Optional.of(rate);
    }
}
