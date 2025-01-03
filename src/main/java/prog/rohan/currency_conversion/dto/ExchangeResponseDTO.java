package prog.rohan.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExchangeResponseDTO {
    private CurrencyResponseDTO baseCurrencyDTO;
    private CurrencyResponseDTO targetCurrencyDTO;
    private Double rate;
    private Integer amount;
    private Double convertedAmount;
}
