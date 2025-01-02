package prog.rohan.currency_conversion.service;

import prog.rohan.currency_conversion.dao.JdbcCurrencyDAO;
import prog.rohan.currency_conversion.dto.CurrencyDTO;
import prog.rohan.currency_conversion.exceptions.DataExistException;
import prog.rohan.currency_conversion.exceptions.DataNotFoundException;
import prog.rohan.currency_conversion.model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CurrencyService {
    private static JdbcCurrencyDAO jdbcCurrencyDAO = JdbcCurrencyDAO.getINSTANCE();

    public static CurrencyDTO insertCurrency(CurrencyDTO currencyDTO){
        Currency currency = new Currency(null, currencyDTO.getCode(),
                currencyDTO.getFullName(), currencyDTO.getSign());
        Optional<Currency> currencyOptional = jdbcCurrencyDAO.findByCode(currencyDTO.getCode());
        if(currencyOptional.isPresent()) throw new DataExistException("Currency with code " +
                                                                      currencyDTO.getCode() + " is already exists!");
        currency = jdbcCurrencyDAO.save(currency);
        return new CurrencyDTO(currency.getId(), currency.getCode(),
                currency.getFullName(), currency.getSign());
    }

    public static List<CurrencyDTO> selectCurrencies(){
        List<CurrencyDTO> currencyDTOList = new ArrayList<>();
        List<Currency> currenciesModelList = jdbcCurrencyDAO.findAll();
        for(Currency model: currenciesModelList){
            currencyDTOList.add(new CurrencyDTO(model.getId(), model.getCode(),
                    model.getFullName(), model.getSign()));
        }
        return currencyDTOList;
    }

    public static CurrencyDTO selectCurrencyByCode(CurrencyDTO currencyDTO){
        Optional<Currency> currencyOptional = jdbcCurrencyDAO.findByCode(currencyDTO.getCode());
        if (currencyOptional.isEmpty()) throw new DataNotFoundException("Currency not found");
        Currency currency = currencyOptional.get();
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }
}
