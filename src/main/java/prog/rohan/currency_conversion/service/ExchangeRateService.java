package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.ExchangeRatesDao;
import prog.rohan.currency_conversion.dto.ExchangeRateDTO;
import prog.rohan.currency_conversion.exceptions.DataExistException;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.model.ExchangeRatesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    public static ExchangeRateDTO insertExchangeRates(ExchangeRateDTO exchangeRateDTO){
        ExchangeRatesModel exchangeRatesModel = new ExchangeRatesModel(null,exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(), exchangeRateDTO.getRate());
        Optional<ExchangeRatesModel> exchangeRateOptional = ExchangeRatesDao.selectByCode(exchangeRatesModel);
        if(exchangeRateOptional.isPresent())
            throw new DataExistException("Exchange rate with code " +
                                         exchangeRateDTO.getBaseCurrencyCode() +
                                         exchangeRateDTO.getTargetCurrencyCode()+
                                         " already exists.");
        exchangeRatesModel = ExchangeRatesDao.insertExchangeRate(exchangeRatesModel);
        return new ExchangeRateDTO(exchangeRatesModel.getId(), exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(), exchangeRatesModel.getRate());
    }

    public static List<ExchangeRateDTO> selectExchangeRates() {
        List<ExchangeRateDTO> exchangeRateDTOS = new ArrayList<>();
        List<ExchangeRatesModel> exchangeRatesModels = ExchangeRatesDao.selectExchangeRates();
        for(ExchangeRatesModel model: exchangeRatesModels){
            exchangeRateDTOS.add(new ExchangeRateDTO(model.getId(),
                    model.getBaseCurrencyCode(),
                    model.getTargetCurrencyCode(),
                    model.getRate()));
        }
        return exchangeRateDTOS;
    }

    public static ExchangeRateDTO selectByCode(ExchangeRateDTO exchangeRateDTO){
        ExchangeRatesModel model = new ExchangeRatesModel(null,
                exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(),
                null);
        Optional<ExchangeRatesModel> exchangeRatesModelOptional = ExchangeRatesDao.selectByCode(model);
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                         exchangeRateDTO.getBaseCurrencyCode() +
                                                                         exchangeRateDTO.getTargetCurrencyCode() +
                                                                         " not found");
        ExchangeRatesModel exchangeRatesModel = exchangeRatesModelOptional.get();
        return new ExchangeRateDTO(exchangeRatesModel.getId(), exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(), exchangeRatesModel.getRate());
    }

    public static ExchangeRateDTO updateExchangeRate(ExchangeRateDTO exchangeRateDTO){
        ExchangeRatesModel exchangeRatesModel = new ExchangeRatesModel(null,exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(), exchangeRateDTO.getRate());
        Optional<ExchangeRatesModel> exchangeRatesModelOptional = ExchangeRatesDao.selectByCode(exchangeRatesModel);
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                                 exchangeRateDTO.getBaseCurrencyCode() +
                                                                                 exchangeRateDTO.getTargetCurrencyCode() +
                                                                                 " not found");
        exchangeRatesModel= ExchangeRatesDao.updateExchangeRate(exchangeRatesModel).get();
        return new ExchangeRateDTO(exchangeRatesModel.getId(),
                exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(),
                exchangeRatesModel.getRate());
    }
}
