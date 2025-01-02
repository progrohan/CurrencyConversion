package prog.rohan.currency_conversion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ExchangeRatesModel {
    private Integer id;
    private String baseCurrencyCode;
    private String TargetCurrencyCode;
    private Double rate;
}
