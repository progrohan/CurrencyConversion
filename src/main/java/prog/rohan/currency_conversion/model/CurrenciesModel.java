package prog.rohan.currency_conversion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrenciesModel {
    private Integer id;
    private String code;
    private String fullName;
    private String sign;
}
