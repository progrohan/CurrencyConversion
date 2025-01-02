package prog.rohan.currency_conversion.dto;

import prog.rohan.currency_conversion.service.CurrencyService;

public class ExchangeDTO {
    private CurrencyDTO baseCurrencyDTO;
    private CurrencyDTO targetCurrencyDTO;
    private Double rate;
    private Integer amount;
    private Double convertedAmount;

    public ExchangeDTO(String baseCurrencyCode, String targetCurrencyCode, Integer amount){
        this.baseCurrencyDTO = CurrencyService.selectCurrencyByCode
                (new CurrencyDTO(null,baseCurrencyCode, null, null));
        this.targetCurrencyDTO = CurrencyService.selectCurrencyByCode
                (new CurrencyDTO(null,targetCurrencyCode, null, null));
        this.amount = amount;
    }

    public ExchangeDTO(CurrencyDTO baseCurrencyDTO, CurrencyDTO targetCurrencyDTO, Double rate, Integer amount, Double convertedAmount) {
        this.baseCurrencyDTO = baseCurrencyDTO;
        this.targetCurrencyDTO = targetCurrencyDTO;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyDTO getBaseCurrencyDTO() {
        return baseCurrencyDTO;
    }

    public void setBaseCurrencyDTO(CurrencyDTO baseCurrencyDTO) {
        this.baseCurrencyDTO = baseCurrencyDTO;
    }

    public CurrencyDTO getTargetCurrencyDTO() {
        return targetCurrencyDTO;
    }

    public void setTargetCurrencyDTO(CurrencyDTO targetCurrencyDTO) {
        this.targetCurrencyDTO = targetCurrencyDTO;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(Double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
