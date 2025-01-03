package prog.rohan.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRateRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private Double rate;
}
