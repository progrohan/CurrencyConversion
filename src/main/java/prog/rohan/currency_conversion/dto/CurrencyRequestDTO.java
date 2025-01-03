package prog.rohan.currency_conversion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyRequestDTO {
    private String code;
    private String fullName;
    private String sign;
}
