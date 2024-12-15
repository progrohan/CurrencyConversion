package prog.rohan.currency_conversion.model;

public class ExchangeRatesModel {
    private Integer id;
    private String baseCurrencyCode;
    private String TargetCurrencyCode;
    private Double rate;

    public ExchangeRatesModel(Integer id, String baseCurrencyCode,
                              String targetCurrencyCode, Double rate) {
        this.id = id;
        this.baseCurrencyCode = baseCurrencyCode;
        this.TargetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return TargetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        TargetCurrencyCode = targetCurrencyCode;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRatesModel{" +
               "id=" + id +
               ", baseCurrencyId=" + baseCurrencyCode +
               ", TargetCurrencyId=" + TargetCurrencyCode +
               ", rate=" + rate +
               '}';
    }
}
