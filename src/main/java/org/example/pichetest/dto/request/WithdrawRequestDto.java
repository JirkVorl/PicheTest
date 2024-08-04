package org.example.pichetest.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WithdrawRequestDto {

    private Long accountNumber;
    private BigDecimal amount;
}
