package prog.rohan.currency_conversion.model;

public class ExchangeRatesModel {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer TargetCurrencyId;
    private Double rate;

    public ExchangeRatesModel(Integer id, Integer baseCurrencyId, Integer targetCurrencyId, Double rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Integer getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
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
               ", baseCurrencyId=" + baseCurrencyId +
               ", TargetCurrencyId=" + TargetCurrencyId +
               ", rate=" + rate +
               '}';
    }
}
