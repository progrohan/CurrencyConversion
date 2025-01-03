package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.JdbcExchangeRateDAO;
import prog.rohan.currency_conversion.dto.CurrencyResponseDTO;
import prog.rohan.currency_conversion.dto.ExchangeRateRequestDTO;
import prog.rohan.currency_conversion.dto.ExchangeRateResponseDTO;
import prog.rohan.currency_conversion.exceptions.DataExistException;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.model.Currency;
import prog.rohan.currency_conversion.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static JdbcExchangeRateDAO jdbcExchangeRateDAO = JdbcExchangeRateDAO.getINSTANCE();

    public static ExchangeRateResponseDTO insertExchangeRates(ExchangeRateRequestDTO exchangeRateDTO){
        ExchangeRate exchangeRatesModel = new ExchangeRate(null,
                new Currency(exchangeRateDTO.getBaseCurrencyCode()),
                new Currency(exchangeRateDTO.getTargetCurrencyCode()),
                exchangeRateDTO.getRate());
        Optional<ExchangeRate> exchangeRateOptional = jdbcExchangeRateDAO.findByCodes(
                exchangeRateDTO.getBaseCurrencyCode(),
                exchangeRateDTO.getTargetCurrencyCode());
        if(exchangeRateOptional.isPresent())
            throw new DataExistException("Exchange rate with code " +
                                         exchangeRateDTO.getBaseCurrencyCode() +
                                         exchangeRateDTO.getTargetCurrencyCode()+
                                         " already exists.");
        exchangeRatesModel = jdbcExchangeRateDAO.save(exchangeRatesModel);
        return new ExchangeRateResponseDTO(
                exchangeRatesModel.getId(),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getBaseCurrency().getId(),
                        exchangeRatesModel.getBaseCurrency().getCode(),
                        exchangeRatesModel.getBaseCurrency().getFullName(),
                        exchangeRatesModel.getBaseCurrency().getSign()),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getTargetCurrency().getId(),
                        exchangeRatesModel.getTargetCurrency().getCode(),
                        exchangeRatesModel.getTargetCurrency().getFullName(),
                        exchangeRatesModel.getTargetCurrency().getSign()),
                exchangeRatesModel.getRate());
    }

    public static List<ExchangeRateResponseDTO> selectExchangeRates() {
        List<ExchangeRateResponseDTO> exchangeRateDTOS = new ArrayList<>();
        List<ExchangeRate> exchangeRatesModels = jdbcExchangeRateDAO.findAll();
        for(ExchangeRate exchangeRatesModel: exchangeRatesModels){
            exchangeRateDTOS.add(new ExchangeRateResponseDTO(
                    exchangeRatesModel.getId(),
                    new CurrencyResponseDTO(
                            exchangeRatesModel.getBaseCurrency().getId(),
                            exchangeRatesModel.getBaseCurrency().getCode(),
                            exchangeRatesModel.getBaseCurrency().getFullName(),
                            exchangeRatesModel.getBaseCurrency().getSign()),
                    new CurrencyResponseDTO(
                            exchangeRatesModel.getTargetCurrency().getId(),
                            exchangeRatesModel.getTargetCurrency().getCode(),
                            exchangeRatesModel.getTargetCurrency().getFullName(),
                            exchangeRatesModel.getTargetCurrency().getSign()),
                    exchangeRatesModel.getRate()));
        }
        return exchangeRateDTOS;
    }

    public static ExchangeRateResponseDTO selectByCode(ExchangeRateRequestDTO exchangeRateDTO){
        ExchangeRate model = new ExchangeRate(
                null,
                new Currency(exchangeRateDTO.getBaseCurrencyCode()),
                new Currency(exchangeRateDTO.getTargetCurrencyCode()),
                null);
        Optional<ExchangeRate> exchangeRatesModelOptional = jdbcExchangeRateDAO.findByCodes(
                model.getBaseCurrency().getCode(),
                model.getTargetCurrency().getCode());
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                         exchangeRateDTO.getBaseCurrencyCode() +
                                                                         exchangeRateDTO.getTargetCurrencyCode() +
                                                                         " not found");
        ExchangeRate exchangeRatesModel = exchangeRatesModelOptional.get();
        return new ExchangeRateResponseDTO(
                exchangeRatesModel.getId(),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getBaseCurrency().getId(),
                        exchangeRatesModel.getBaseCurrency().getCode(),
                        exchangeRatesModel.getBaseCurrency().getFullName(),
                        exchangeRatesModel.getBaseCurrency().getSign()),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getTargetCurrency().getId(),
                        exchangeRatesModel.getTargetCurrency().getCode(),
                        exchangeRatesModel.getTargetCurrency().getFullName(),
                        exchangeRatesModel.getTargetCurrency().getSign()),
                exchangeRatesModel.getRate());
    }

    public static ExchangeRateResponseDTO updateExchangeRate(ExchangeRateRequestDTO exchangeRateDTO){
        ExchangeRate exchangeRatesModel = new ExchangeRate(null,
                new Currency(exchangeRateDTO.getBaseCurrencyCode()),
                new Currency(exchangeRateDTO.getTargetCurrencyCode()),
                exchangeRateDTO.getRate());
        Optional<ExchangeRate> exchangeRatesModelOptional = jdbcExchangeRateDAO.findByCodes(
                exchangeRatesModel.getBaseCurrency().getCode(),
                exchangeRatesModel.getTargetCurrency().getCode());
        if(exchangeRatesModelOptional.isEmpty()) throw new DataNotFoundException("Exchange rate with code " +
                                                                                 exchangeRateDTO.getBaseCurrencyCode() +
                                                                                 exchangeRateDTO.getTargetCurrencyCode() +
                                                                                 " not found");
        exchangeRatesModel= jdbcExchangeRateDAO.update(exchangeRatesModel).get();
        return new ExchangeRateResponseDTO(
                exchangeRatesModel.getId(),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getBaseCurrency().getId(),
                        exchangeRatesModel.getBaseCurrency().getCode(),
                        exchangeRatesModel.getBaseCurrency().getFullName(),
                        exchangeRatesModel.getBaseCurrency().getSign()),
                new CurrencyResponseDTO(
                        exchangeRatesModel.getTargetCurrency().getId(),
                        exchangeRatesModel.getTargetCurrency().getCode(),
                        exchangeRatesModel.getTargetCurrency().getFullName(),
                        exchangeRatesModel.getTargetCurrency().getSign()),
                exchangeRatesModel.getRate());
    }
}
