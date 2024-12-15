package prog.rohan.currency_conversion.dto;

public class ExchangeRateDTO {
    private Integer id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private Double rate;

    public ExchangeRateDTO(int id, String baseCurrencyCode, String targetCurrencyCode, double rate) {
        this.id = id;
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public ExchangeRateDTO(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public double getRate() {
        return rate;
    }
}
