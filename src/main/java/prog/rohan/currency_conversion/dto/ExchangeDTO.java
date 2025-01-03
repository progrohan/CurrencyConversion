package prog.rohan.currency_conversion.dto;

import prog.rohan.currency_conversion.service.CurrencyService;

public class ExchangeDTO {
    private CurrencyResponseDTO baseCurrencyDTO;
    private CurrencyResponseDTO targetCurrencyDTO;
    private Double rate;
    private Integer amount;
    private Double convertedAmount;

    public ExchangeDTO(String baseCurrencyCode, String targetCurrencyCode, Integer amount){
        this.baseCurrencyDTO = CurrencyService.selectCurrencyByCode
                (new CurrencyRequestDTO(baseCurrencyCode, null, null));
        this.targetCurrencyDTO = CurrencyService.selectCurrencyByCode
                (new CurrencyRequestDTO(targetCurrencyCode, null, null));
        this.amount = amount;
    }

    public ExchangeDTO(CurrencyResponseDTO baseCurrencyDTO, CurrencyResponseDTO targetCurrencyDTO, Double rate, Integer amount, Double convertedAmount) {
        this.baseCurrencyDTO = baseCurrencyDTO;
        this.targetCurrencyDTO = targetCurrencyDTO;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyResponseDTO getBaseCurrencyDTO() {
        return baseCurrencyDTO;
    }

    public void setBaseCurrencyDTO(CurrencyResponseDTO baseCurrencyDTO) {
        this.baseCurrencyDTO = baseCurrencyDTO;
    }

    public CurrencyResponseDTO getTargetCurrencyDTO() {
        return targetCurrencyDTO;
    }

    public void setTargetCurrencyDTO(CurrencyResponseDTO targetCurrencyDTO) {
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
