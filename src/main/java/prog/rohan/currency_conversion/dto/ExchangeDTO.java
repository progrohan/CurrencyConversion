package prog.rohan.currency_conversion.dto;

public class ExchangeDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private Double amount;

    public ExchangeDTO(String baseCurrencyCode, String targetCurrencyCode){
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public Double getAmount(){
        return amount;
    }

    public void setAmount(Double amount){
        this.amount = amount;
    }
}
