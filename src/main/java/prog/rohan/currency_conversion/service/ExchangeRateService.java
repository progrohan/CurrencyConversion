package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.JdbcExchangeRateDAO;
import prog.rohan.currency_conversion.dto.ExchangeRateDTO;
import prog.rohan.currency_conversion.exceptions.DataExistException;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static JdbcExchangeRateDAO jdbcExchangeRateDAO = JdbcExchangeRateDAO.getINSTANCE();

    public static ExchangeRateDTO insertExchangeRates(ExchangeRateDTO exchangeRateDTO){
        ExchangeRate exchangeRatesModel = new ExchangeRate(null,exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(), exchangeRateDTO.getRate());
        Optional<ExchangeRate> exchangeRateOptional = jdbcExchangeRateDAO.findByCodes(exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode());
        if(exchangeRateOptional.isPresent())
            throw new DataExistException("Exchange rate with code " +
                                         exchangeRateDTO.getBaseCurrencyCode() +
                                         exchangeRateDTO.getTargetCurrencyCode()+
                                         " already exists.");
        exchangeRatesModel = jdbcExchangeRateDAO.save(exchangeRatesModel);
        return new ExchangeRateDTO(exchangeRatesModel.getId(), exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(), exchangeRatesModel.getRate());
    }

    public static List<ExchangeRateDTO> selectExchangeRates() {
        List<ExchangeRateDTO> exchangeRateDTOS = new ArrayList<>();
        List<ExchangeRate> exchangeRatesModels = jdbcExchangeRateDAO.findAll();
        for(ExchangeRate model: exchangeRatesModels){
            exchangeRateDTOS.add(new ExchangeRateDTO(model.getId(),
                    model.getBaseCurrencyCode(),
                    model.getTargetCurrencyCode(),
                    model.getRate()));
        }
        return exchangeRateDTOS;
    }

    public static ExchangeRateDTO selectByCode(ExchangeRateDTO exchangeRateDTO){
        ExchangeRate model = new ExchangeRate(null,
                exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(),
                null);
        Optional<ExchangeRate> exchangeRatesModelOptional = jdbcExchangeRateDAO.findByCodes(
                model.getBaseCurrencyCode(),
                model.getTargetCurrencyCode());
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                         exchangeRateDTO.getBaseCurrencyCode() +
                                                                         exchangeRateDTO.getTargetCurrencyCode() +
                                                                         " not found");
        ExchangeRate exchangeRatesModel = exchangeRatesModelOptional.get();
        return new ExchangeRateDTO(exchangeRatesModel.getId(), exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(), exchangeRatesModel.getRate());
    }

    public static ExchangeRateDTO updateExchangeRate(ExchangeRateDTO exchangeRateDTO){
        ExchangeRate exchangeRatesModel = new ExchangeRate(null,exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode(), exchangeRateDTO.getRate());
        Optional<ExchangeRate> exchangeRatesModelOptional = jdbcExchangeRateDAO.findByCodes(
                exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode());
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                                 exchangeRateDTO.getBaseCurrencyCode() +
                                                                                 exchangeRateDTO.getTargetCurrencyCode() +
                                                                                 " not found");
        exchangeRatesModel= jdbcExchangeRateDAO.update(exchangeRatesModel).get();
        return new ExchangeRateDTO(exchangeRatesModel.getId(),
                exchangeRatesModel.getBaseCurrencyCode(),
                exchangeRatesModel.getTargetCurrencyCode(),
                exchangeRatesModel.getRate());
    }
}
