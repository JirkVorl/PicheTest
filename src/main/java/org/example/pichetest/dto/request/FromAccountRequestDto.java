package org.example.pichetest.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FromAccountRequestDto {

    private Long accountNumber;
    private BigDecimal amount;
}
