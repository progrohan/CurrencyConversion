package prog.rohan.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeRequestDTO {
    String baseCurrencyCode;
    String targetCurrencyCode;
    Integer amount;
}
